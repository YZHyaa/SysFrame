package com.mdt.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.mdt.listener.PropertyListener;
/**
 * 视频转码工具类
 * @author hou
 *
 */
public class VideoTranslateUtil {
     public static final String FFMPEG_PATH = PropertyListener.getPropertyValue("${system.ffmpeg_path}");
     private static Process process;  
	 public static String convertCommand(String video_path) {  
	        //D:/ffmpeg.exe -i D:\360Downloads\7049a5246a2d44fe897c2ea1c917eeee.wmv -vcodec libx264 -preset ultrafast -profile:v baseline -acodec aac -strict experimental -s 640*480 -b 568k -ab 128k iCulture.mp4  
	       if(StringUtil.isEmpty(video_path)){  
	            return null;  
	        }  
	        File file = new File(video_path);  
	        if (!file.exists()) {  
	            System.err.println("路径[" + video_path + "]对应的视频文件不存在!");  
	            return null;  
	        }  
	        String videoUrl = "";  
	        String format = ".mp4";  
	        try {  
//	            String videoName = videoFileName.substring(0,videoFileName.lastIndexOf("."))+format;  
//	            String newVideoPath = video_path.substring(0,video_path.lastIndexOf("/")+1)+videoName;  
	            String newVideoPath = video_path.substring(0,video_path.lastIndexOf("."))+format;  
	          
	            //  String videoName = videoFileName.substring(0,videoFileName.lastIndexOf("."))+format;  
	            Integer type = checkVideoType(video_path);  
	            if(0==type){  
	                List<String> commands = new java.util.ArrayList<String>();  
	                commands.add(FFMPEG_PATH);  
	                commands.add("-i");  
	                commands.add(video_path);  
	                commands.add("-vcodec");  
	                commands.add("libx264"); 
	                commands.add("-r");
	                commands.add("25");
	                commands.add("-pix_fmt");  
	                commands.add("yuv420p"); 
	                commands.add("-profile");  
	                commands.add("main"); 
	                commands.add("-c:a");  
	                commands.add("aac"); 
	                commands.add("-strict");  
	                commands.add("-2"); 
//	                commands.add("-preset");  
//	                commands.add("ultrafast");  
//	                commands.add("-profile:v");  
//	                commands.add("baseline");  
//	                commands.add("-acodec");  
//	                commands.add("aac");  
//	                commands.add("-strict");  
//	                commands.add("experimental");  
////	                commands.add("-s");  
////	                commands.add("640*480");  
////	                commands.add("-b");//视频品质设置（有模糊，要视频清晰使用-qscale）  
////	                commands.add("568k");  
//	                commands.add("-qscale");//视频品质  
//	                commands.add("6");//视频品质参数  
//	                commands.add("-ab");  
//	                commands.add("128k");  
//	                commands.add("-y");//文件存在选择重写  
//	                commands.add("-vcodec");
//	                commands.add("-vcodec");
//	                commands.add("h264");
//	                commands.add("-b");//视频品质设置（有模糊，要视频清晰使用-qscale）  
//	                commands.add("300k");  
	                commands.add(newVideoPath);  
	                ProcessBuilder builder = new ProcessBuilder();  
	                builder.command(commands);  
	                process = builder.start();  
	                //process.waitFor();//等待进程执行完毕  
	                //防止ffmpeg进程塞满缓存造成死锁  
	                InputStream error = process.getErrorStream();  
	                InputStream is = process.getInputStream();  
	                byte[] b = new byte[1024];  
	                int readbytes = -1;  
	                try {  
	                    while((readbytes = error.read(b)) != -1){  
	                    	System.out.println("FFMPEG视频转换进程错误信息："+new String(b,0,readbytes));  
	                    }  
	                    while((readbytes = is.read(b)) != -1){  
	                        System.out.println("FFMPEG视频转换进程输出内容为："+new String(b,0,readbytes));  
	                    }  
	                }catch (IOException e2){  
	  
	                }finally {  
	                    error.close();  
	                    is.close();  
	                }  
	            }  
	        } catch (Exception e) {  
	            e.printStackTrace();  
	        }  
	        return videoUrl;  
	    }  
	  
	    public static Integer checkVideoType(String PATH) {  
	        String type = PATH.substring(PATH.lastIndexOf(".") + 1, PATH.length())  
	                .toLowerCase();  
	        // ffmpeg能解析的格式：（asx，asf，mpg，wmv，3gp，mp4，mov，avi，flv等）  
	        if (type.equals("avi")) {  
	            return 0;  
	        } else if (type.equals("mpg")) {  
	            return 0;  
	        } else if (type.equals("wmv")) {  
	            return 0;  
	        } else if (type.equals("3gp")) {  
	            return 0;  
	        } else if (type.equals("mov")) {  
	            return 0;  
	        } else if (type.equals("mp4")) {  
	            return 9;//本身是MP4格式不用转换  
	        } else if (type.equals("asf")) {  
	            return 0;  
	        } else if (type.equals("asx")) {  
	            return 0;  
	        } else if (type.equals("flv")) {  
	            return 0;  
	        }  
	        // 对ffmpeg无法解析的文件格式(wmv9，rm，rmvb等),  
	        else if (type.equals("wmv9")) {  
	            return 1;  
	        } else if (type.equals("rm")) {  
	            return 1;  
	        } else if (type.equals("rmvb")) {  
	            return 1;  
	        }  
	        return 9;  
	    }  
	    
	    public static void main(String[] args) {
	    	convertCommand("C:\\Picture\\hou.wmv");
		}
}
