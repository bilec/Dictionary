package com.example.dictionary.recyclerView.wordEntry;

import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;

import com.example.dictionary.data.wordentry.WordEntry;
import com.example.dictionary.databinding.RecyclerviewItemHomeBinding;
import com.example.dictionary.html.HtmlTagHandler;


public class TextViewHolder extends AbstractViewHolder {

    private RecyclerviewItemHomeBinding binding;

    public TextViewHolder(@NonNull RecyclerviewItemHomeBinding binding)
    {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void bindView(WordEntry wordEntry)
    {
        binding.textViewDictionaryName.setText(wordEntry.getDictionaryName());

        String word = wordEntry.getWord();

        int flags =
//                HtmlCompat.FROM_HTML_MODE_COMPACT //&
                HtmlCompat.FROM_HTML_MODE_LEGACY //&
//                HtmlCompat.FROM_HTML_SEPARATOR_LINE_BREAK_DIV &
//                HtmlCompat.FROM_HTML_SEPARATOR_LINE_BREAK_HEADING &
//                HtmlCompat.FROM_HTML_SEPARATOR_LINE_BREAK_LIST &
//                HtmlCompat.FROM_HTML_SEPARATOR_LINE_BREAK_LIST_ITEM &
//                HtmlCompat.FROM_HTML_SEPARATOR_LINE_BREAK_PARAGRAPH
                ;


        binding.textView.setText(HtmlCompat.fromHtml(word,flags,null, new HtmlTagHandler()));

    }

}