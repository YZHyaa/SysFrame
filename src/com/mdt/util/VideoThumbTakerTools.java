package com.mdt.util;



import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mdt.listener.PropertyListener;

public class VideoThumbTakerTools {
	
    //ffmpeg��ѹ·��
    private static String ffmpegApp;
 
    
    public VideoThumbTakerTools(String ffmpegApp)
    {
        this.ffmpegApp = ffmpegApp;
    }
  
		

    /****
     * ��ȡָ��ʱ���ڵķ���ͼƬ
     * @param videoFilename:��Ƶ·��
     * @param thumbFilename:ͼƬ����·��
     * @param width:ͼƬ��
     * @param height:ͼƬ��
     * @param hour:ָ��ʱ
     * @param min:ָ����
     * @param sec:ָ����
     * @throws IOException
     * @throws InterruptedException
     */
    public static void getThumb(String videoFilename, String thumbFilename, int width,
            int height, int hour, int min, float sec) throws IOException,
            InterruptedException
    {
        ProcessBuilder processBuilder = new ProcessBuilder(ffmpegApp, "-y",
                "-i", videoFilename, "-vframes", "1", "-ss", hour + ":" + min
                        + ":" + sec, "-f", "mjpeg", "-an", thumbFilename);

        Process process = processBuilder.start();

        InputStream stderr = process.getErrorStream();
        InputStreamReader isr = new InputStreamReader(stderr);
        BufferedReader br = new BufferedReader(isr);
        String line;
        while ((line = br.readLine()) != null)
            ;
        process.waitFor();
        
        if(br != null)
            br.close();
        if(isr != null)
            isr.close();
        if(stderr != null)
            stderr.close();
    }
    
    
	public static void main(String[] args)
    {
    	VideoThumbTakerTools videoThumbTakerTools = new VideoThumbTakerTools("C:\\ffmpeg\\ffmpeg.exe");
        try
        {
        	//��ȡ��һ֡
//        	videoThumbTakerTools.getThumb("C:\\photobag\\test1.avi", "C:\\photobag\\thumbTest.png", 800, 600, 0, 0, 1);	
            System.out.println("over");
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    

    
}
