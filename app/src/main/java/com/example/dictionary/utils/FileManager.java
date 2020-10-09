package com.example.dictionary.utils;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.core.content.FileProvider;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

public class FileManager {

    private Context context;

    public FileManager(Context context)
    {
        this.context = context;
    }

    public void downloadFile(String url, String fileName)
    {
        File outputFile = new File(getDir(), fileName);

        try {
            URL u = new URL(url);
            URLConnection conn = u.openConnection();
            int contentLength = conn.getContentLength();

            DataInputStream stream = new DataInputStream(u.openStream());

            byte[] buffer = new byte[contentLength];
            stream.readFully(buffer);
            stream.close();

            DataOutputStream fos = new DataOutputStream(new FileOutputStream(outputFile));
            fos.write(buffer);
            fos.flush();
            fos.close();
        } catch(IOException e) {
            return;
        }
    }

    public void deleteFile(String fileName)
    {
        File fileToDelete = new File(getDir(),fileName);

        if(fileToDelete.exists())
        {
            boolean delete = fileToDelete.delete();
        }
    }

    public String getFilePath(String fileName)
    {
        File file = new File(getDir(), fileName);
        return file.getPath();
    }

    public Uri getFileUri(String fileName)
    {
        File file = new File(getDir(), fileName);
        return FileProvider.getUriForFile(context,context.getApplicationContext().getPackageName()+".provider",file);
    }

    private File getDir()
    {
        return context.getFilesDir();
    }

}
