package com.example.dictionary.work;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.webkit.MimeTypeMap;
import android.webkit.URLUtil;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.dictionary.R;
import com.example.dictionary.data.word.Word;
import com.example.dictionary.data.word.history.global.GlobalWordRepository;
import com.example.dictionary.data.wordentry.WordEntry;
import com.example.dictionary.data.wordentry.WordEntryRepository;
import com.example.dictionary.utils.FileManager;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WordSearchWorker extends Worker {

    public static final String WORD = "WORD";
    public static final String SOUND = "SOUND";
    public static final String IMAGES = "IMAGES";

    private static final String REQUEST_URL = "https://slovnik.juls.savba.sk/";
    private static final String SPECIAL_CODE = "SPECIAL_CODE";

    private WordEntryRepository mWordEntryRepository;
    private GlobalWordRepository mGlobalWordRepository;

    private Context context;

    public WordSearchWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;

        mWordEntryRepository = new WordEntryRepository(context);
        mGlobalWordRepository = new GlobalWordRepository(context);
    }


    @NonNull
    @Override
    public Result doWork() {

        String wordToSearch = getInputData().getString(WORD);
        String soundToDelete = getInputData().getString(SOUND);
        String[] imagesToDelete = getInputData().getStringArray(IMAGES);

        String specialCodeForThisTime = getStringFromSharedPreferences(SPECIAL_CODE, "");
        if(specialCodeForThisTime.isEmpty())
        {
            specialCodeForThisTime = requestSpecialCode();
            if(specialCodeForThisTime.isEmpty())
            {
                return Result.failure();
            }
        }



        String url = urlBuilder(wordToSearch,specialCodeForThisTime);

        Document doc;
        try {
            doc = Jsoup.connect(url)
                    .timeout(10000)
                    .header("Accept-Encoding", "gzip, deflate")
                    .get();
        } catch (IOException e) {
            return Result.failure();
        }

        Element body = doc.body();

        String specialCodeForNextTime = getSpecialCode(body);
        if(!specialCodeForNextTime.isEmpty())
        {
            saveToSharedPreferences(SPECIAL_CODE, specialCodeForNextTime);
        }

        List<WordEntry> wordEntryList = new ArrayList<>();
        List<Word> onlineWordList = getOnlineWords(body.getElementsByTag("footer").first());

        Elements sections = body.getElementsByTag( "section" );
        for(Element section : sections)
        {
            String dictionaryName;
            String word;
            List<String> imageNameList;
            String soundName;

            Element resultHead = section.getElementsByClass( "resulthead").first();
            Element resultBody = section.getElementsByClass( "resultbody" ).first();

            dictionaryName = getDictionaryName(resultHead);
            word = getWord(resultBody);
            imageNameList = getImageNameList(resultBody);
            soundName = getSoundName(resultBody);


            WordEntry wordEntry = new WordEntry(dictionaryName,word,imageNameList,soundName);
            wordEntryList.add(wordEntry);
        }

        deleteFiles(soundToDelete, imagesToDelete);

        if(wordEntryList.isEmpty())
        {
            return Result.failure();
        }

        updateWordEntryDatabase(wordEntryList);

        updateOnlineWordDatabase(onlineWordList);

        return Result.success();
    }

    private String getDictionaryName(Element resultHead)
    {
        Element dicName = resultHead.getElementsByTag("h3").first();
        return dicName.text();
    }

    private String getWord(Element sectionBody)
    {
        Elements scriptTags = sectionBody.getElementsByTag("script");
        for(Element scriptTag : scriptTags)
        {
            scriptTag.text("");
        }

        Elements svgTags = sectionBody.getElementsByTag("svg");
        for(Element svgTag : svgTags)
        {
            svgTag.text("");
        }

        int tableCounter = 0;
        Elements tableTags = sectionBody.getElementsByTag("table");
        for(Element tableTag : tableTags)
        {
            if(tableTag.attr("class").equals("lohi"))
            {
                break;
            }


            tableTag.prepend("<br>");

            tableCounter = tableCounter + 1;
            if((tableCounter % 2) != 0) {
                tableTag.append("<br>");
            }
            Elements trTags = tableTag.getElementsByTag("tr");
            for(Element trTag : trTags)
            {
                StringBuilder stringBuilder = new StringBuilder();
                Elements tdTags = trTag.getElementsByTag("td");
                for(Element tdTag : tdTags)
                {
                    boolean kwicl = tdTag.attr("class").equals("kwicl");
                    boolean kwic = tdTag.attr("class").equals("kwic");
                    boolean kwicr = tdTag.attr("class").equals("kwicr");
                    boolean crpinf = tdTag.attr("class").equals("crpinf");

                    if(kwicl || kwic || kwicr || crpinf)
                    {
                        tdTag.text("");
                    }
                    else {
                        stringBuilder.append(tdTag.text());
                        tdTag.append("&nbsp;&nbsp;");
                    }
                }
                trTag.append("<br>");
            }
        }


        String word = sectionBody.html();
        word = word.replaceAll("<[aA].*?>", "<u>");
        word = word.replaceAll("</[aA]>", "</u>");

        word = replaceLast(word,"<p>","");
        word = replaceLast(word,"</p>","");

        return word;
    }

    private List<String> getImageNameList(Element sectionBody)
    {
        List<String> imageNameList = null;
        Elements images = sectionBody.select("img");
        if(images.size() > 0)
        {
            imageNameList = new ArrayList<>();
        }
        for(Element image : images)
        {
            String imageSrc = image.absUrl("src");
            String imageName = getFileNameFromUrl(imageSrc);

            FileManager fileManager = new FileManager(context);
            fileManager.downloadFile(imageSrc, imageName);
            imageNameList.add(imageName);
        }

        return imageNameList;
    }

    private String getSoundName(Element sectionBody)
    {
        String soundName = null;
        Element sound = sectionBody.getElementsByTag("audio").first();
        if(sound != null) {
            Elements soundSources = sound.getElementsByTag("source");
            for (Element soundSource : soundSources) {
                if(soundSource.attr("type").equals("audio/ogg"))
                {
                    String soundSrc = soundSource.absUrl("src");
                    soundName = getFileNameFromUrl(soundSrc);

                    FileManager fileManager = new FileManager(context);
                    fileManager.downloadFile(soundSrc, soundName);

                }
            }
        }

        return soundName;
    }

    private List<Word> getOnlineWords(Element footer)
    {
        List<Word> onlineWordList = new ArrayList<>();

        Element lastWord = footer.getElementsByClass("lastwords").first();
        Elements spanTags = lastWord.getElementsByTag("span");

        Pattern pattern = Pattern.compile("\\p{L}+");
        for(Element spanTag : spanTags)
        {
            Matcher matcher = pattern.matcher(spanTag.text());
            if(matcher.find())
            {
                onlineWordList.add(new Word(matcher.group()));
            }
        }

        return onlineWordList;
    }

    private String getSpecialCode(Element sectionBody)
    {
        String retSpecialCode = "";
        Elements inputC = sectionBody.select("input[name=c]");
        for(Element element : inputC)
        {
            retSpecialCode = element.attr("value");
            if(!retSpecialCode.isEmpty())
            {
                break;
            }
        }

        return retSpecialCode;
    }


    private void deleteFiles(String soundToDelete, String[] imagesToDelete)
    {
        FileManager fileManager = new FileManager(context);

        if(soundToDelete != null)
        {
            fileManager.deleteFile(soundToDelete);
        }

        if(imagesToDelete != null)
        {
            for(String imageToDelete : imagesToDelete)
            {
                fileManager.deleteFile(imageToDelete);
            }
        }
    }

    private void updateWordEntryDatabase(List<WordEntry> wordEntryList)
    {
        mWordEntryRepository.deleteAllWithoutExecutor();
        int id = 0;
        for(WordEntry wordEntry : wordEntryList)
        {
            wordEntry.setId(id);
            id++;
            mWordEntryRepository.insertWithoutExecutor(wordEntry);
        }
    }

    private void updateOnlineWordDatabase(List<Word> onlineWordList)
    {
        mGlobalWordRepository.deleteAllWithoutExecutor();
        for(Word word : onlineWordList)
        {
            mGlobalWordRepository.insertWithoutExecutor(word);
        }
    }

    private void saveToSharedPreferences(String key, String value)
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(key, value);
        editor.apply();
    }

    private String getStringFromSharedPreferences(String key, String defaultValue)
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(key, defaultValue);
    }


    private String requestSpecialCode()
    {
        String retText = "";
        Document doc;
        try {
            doc = Jsoup.connect(REQUEST_URL).get();
        } catch (IOException e) {
            return retText;
        }

        Element body = doc.body();
        retText = getSpecialCode(body);

        return retText;
    }

    private String urlBuilder(String word, String specialText)
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        String searchPattern = sharedPreferences.getString(context.getString(R.string.settings_search_pattern_key),context.getString(R.string.settings_search_pattern_default));

        String kssjDicString = context.getString(R.string.settings_dictionary_kssj_value);
        String pspDicString = context.getString(R.string.settings_dictionary_psp_value);
        String sssjDicString = context.getString(R.string.settings_dictionary_sssj_value);
        String orterDicString = context.getString(R.string.settings_dictionary_orter_value);
        String scsDicString = context.getString(R.string.settings_dictionary_scs_value);
        String sssDicString = context.getString(R.string.settings_dictionary_sss_value);
        String peciarDicString = context.getString(R.string.settings_dictionary_peciar_value);

        String hssjVDicString = context.getString(R.string.settings_dictionary_hssjV_value);
        String bernolakDicString = context.getString(R.string.settings_dictionary_bernolak_value);

        String noundbDicString = context.getString(R.string.settings_dictionary_noundb_value);
        String orientDicString = context.getString(R.string.settings_dictionary_orient_value);
        String locutioDicString = context.getString(R.string.settings_dictionary_locutio_value);
        String obceDicString = context.getString(R.string.settings_dictionary_obce_value);
        String priezviskaDicString = context.getString(R.string.settings_dictionary_priezviska_value);
        String unDicString = context.getString(R.string.settings_dictionary_un_value);
        String pskcsDicString = context.getString(R.string.settings_dictionary_pskcs_value);
        String pskenDicString = context.getString(R.string.settings_dictionary_psken_value);


        boolean kssjDicBool = sharedPreferences.getBoolean(kssjDicString,true);
        boolean pspDicBool = sharedPreferences.getBoolean(pspDicString,true);
        boolean sssjDicBool = sharedPreferences.getBoolean(sssjDicString,true);
        boolean orterDicBool = sharedPreferences.getBoolean(orterDicString,true);
        boolean scsDicBool = sharedPreferences.getBoolean(scsDicString,true);
        boolean sssDicBool = sharedPreferences.getBoolean(sssDicString,true);
        boolean peciarDicBool = sharedPreferences.getBoolean(peciarDicString,true);

        boolean hssjVDicBool = sharedPreferences.getBoolean(hssjVDicString,true);
        boolean bernolakDicBool = sharedPreferences.getBoolean(bernolakDicString,true);

        boolean noundbDicBool = sharedPreferences.getBoolean(noundbDicString,true);
        boolean orientDicBool = sharedPreferences.getBoolean(orientDicString,true);
        boolean locutioDicBool = sharedPreferences.getBoolean(locutioDicString,true);
        boolean obceDicBool = sharedPreferences.getBoolean(obceDicString,true);
        boolean priezviskaDicBool = sharedPreferences.getBoolean(priezviskaDicString,true);
        boolean unDicBool = sharedPreferences.getBoolean(unDicString,true);
        boolean pskcsDicBool = sharedPreferences.getBoolean(pskcsDicString,true);
        boolean pskenDicBool = sharedPreferences.getBoolean(pskenDicString,true);

        Uri baseUri = Uri.parse(REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("w",word);
        uriBuilder.appendQueryParameter("c",specialText);

        uriBuilder.appendQueryParameter("s",searchPattern);

        if(kssjDicBool) uriBuilder.appendQueryParameter("d",kssjDicString);
        if(pspDicBool) uriBuilder.appendQueryParameter("d",pspDicString);
        if(sssjDicBool) uriBuilder.appendQueryParameter("d",sssjDicString);
        if(orterDicBool) uriBuilder.appendQueryParameter("d",orterDicString);
        if(scsDicBool) uriBuilder.appendQueryParameter("d",scsDicString);
        if(sssDicBool) uriBuilder.appendQueryParameter("d",sssDicString);
        if(peciarDicBool) uriBuilder.appendQueryParameter("d",peciarDicString);

        if(hssjVDicBool) uriBuilder.appendQueryParameter("d",hssjVDicString);
        if(bernolakDicBool) uriBuilder.appendQueryParameter("d",bernolakDicString);

        if(noundbDicBool) uriBuilder.appendQueryParameter("d",noundbDicString);
        if(orientDicBool) uriBuilder.appendQueryParameter("d",orientDicString);
        if(locutioDicBool) uriBuilder.appendQueryParameter("d",locutioDicString);
        if(obceDicBool) uriBuilder.appendQueryParameter("d",obceDicString);
        if(priezviskaDicBool) uriBuilder.appendQueryParameter("d",priezviskaDicString);
        if(unDicBool) uriBuilder.appendQueryParameter("d",unDicString);
        if(pskcsDicBool) uriBuilder.appendQueryParameter("d",pskcsDicString);
        if(pskenDicBool) uriBuilder.appendQueryParameter("d",pskenDicString);

        return uriBuilder.toString();
    }

    /**
     * From
     * https://stackoverflow.com/a/10522801
     */
    private String getFileNameFromUrl(String urlText)
    {
        String fileExtension = MimeTypeMap.getFileExtensionFromUrl(urlText);
        return URLUtil.guessFileName(urlText, null,fileExtension);
    }


    /**
     * From
     * https://stackoverflow.com/a/5254817
     */
    private String replaceLast(String text, String from, String to)
    {
        int lastIndex = text.lastIndexOf(from);
        if(lastIndex < 0)
        {
            return text;
        }

        String tail = text.substring(lastIndex).replaceFirst(from, to);
        return text.substring(0, lastIndex) + tail;
    }

}
