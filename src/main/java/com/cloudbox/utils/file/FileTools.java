package com.cloudbox.utils.file;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.io.FileUtils;

import com.cloudbox.utils.StringTools;

/**
 * 文件处理工具类
 * 
 * @author YangLong
 * 
 */
public class FileTools {

    /********************************************************* 文件（夹）-增、删、改、查 *****************************************************************/
    /**
     * 增-创建文件夹：根据指定路径创建，路径包含文件夹名
     * 
     * @param folderPath
     *            -文件夹路径
     * @example 在"C:/"目录下创建文件夹newFolder，则folderPath="C:/newFolder"
     * @return
     */
    public static String createFolderByPath(String folderPath) {
        String flag = "0";
        if (ifFileExist(folderPath.substring(0, folderPath.lastIndexOf("/"))) == false) {
            createFolderByPath(folderPath.substring(0,
                    folderPath.lastIndexOf("/")));
        }
        File folder = new File(folderPath);
        if (folder.exists()) {
            // 已存在同名文件夹
            flag = "1";
        } else {
            folder.mkdir();
            flag = "2";
        }
        return flag;
    }

    /**
     * 增-创建文件：根据文件及目标路径参数，创建文件
     */
    public static void createFileByFile(File file, String targetPath)
            throws Exception {
        FileOutputStream fos = new FileOutputStream(targetPath);
        FileInputStream fis = new FileInputStream(file);
        byte[] buffer = new byte[1024];// 2*1024 * 1024
        int length = 0;
        while ((length = fis.read(buffer)) > 0) {
            fos.write(buffer, 0, length);
        }
        fis.close();
        fos.close();
    }

    /**
     * 删-删除文件（夹）：根据指定的文件删除文件（夹）
     * 
     * @param file
     *            -文件
     * @example 在"C:/"目录下删除文件夹newFolder，则file="new File('C:/newFolder')"
     *          在"C:/"目录下删除文件newFile.txt，则file="new File('C:/newFile.txt')"
     * @return
     */
    public static String delete(File file) {
        String flag = "0";
        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
                flag = "1";
            } else if (file.isDirectory()) {
                File[] files = file.listFiles();
                for (File f : files) {
                    delete(f);
                }
                file.delete();
                flag = "2";
            }
        } else {
            flag = "3";
        }
        return flag;
    }

    /**
     * 构建JSON格式的目录结构，为folderRecursion()方法提供服务
     */
    public static JSONArray getJSON(int startNumber, JSONArray json,
            String path, String[] dataPaths) {
        File f = new File(path);
        File[] fList = f.listFiles();
        int count = fList.length;
        boolean flag = true;
        JSONObject node = new JSONObject();
        for (int i = 0; i < count; i++) {
            if (fList[i].isFile() == false) {
                String route =StringTools.Replace(fList[i].getPath()
                        .substring(startNumber).toString(), "\\", "/");
                node.put("data", fList[i].getName());
                node.put("metadata", "{path:'" + route + "'}");
                node.put(
                        "children",
                        getJSON(startNumber, new JSONArray(),
                                fList[i].getPath(), dataPaths));
                for (int j = 0; j < dataPaths.length; j++) {
                    if (dataPaths[j].equals(route)) {
                        flag = false;
                    }
                }
                if (flag == true) {
                    json.add(node);
                }
                flag = true;
            }
        }
        return json;
    }

    /**
     * 改-移动文件（夹）
     * 
     * @param fileName
     *            -文件名, oldPath-旧文件路径, newPath-新文件路径
     * @return
     */
    public static String move(String fileName, String oldPath, String newPath) {
        String flag = "0";
        File oldfile = new File(oldPath);
        File newfile = new File(newPath);
        if (newfile.exists()) {
            // 新位置已有同名数据
            flag = "1";
        } else {
            if (oldfile.exists()) {
                // 移动数据至新的位置
                oldfile.renameTo(newfile);
                flag = "2";
            } else {
                // 原始数据不存在
                flag = "3";
            }
        }
        return flag;
    }

    public static String copy(String fileName, String oldPath, String newPath) {
        String flag = "0";
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) { // 文件存在时
                InputStream inStream = new FileInputStream(oldPath); // 读入原文件
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];

                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; // 字节数 文件大小
                    System.out.println(bytesum);
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
            }
        } catch (Exception e) {
            System.out.println("复制单个文件操作出错");
            e.printStackTrace();

        }
        return flag;
    }

    /**
     * 改-重命名文件（夹）：
     * 
     * @param oldPath
     *            -旧文件路径, newPath-新文件路径
     * @return
     */
    public static String rename(String oldPath, String newPath) {
        String flag = "0";
        File oldfile = new File(oldPath);
        File newfile = new File(newPath);
        if (oldfile.exists()) {
            if (newfile.exists()) {
                flag = "1";
            } else {
                oldfile.renameTo(newfile);
            }
        } else {
            flag = "2";
        }
        return flag;
    }

    public static String getFileType(Object o) {
        System.out.println("getFileType o: " + o);
        try {
            // Create an image input stream on the image
            ImageInputStream iis = ImageIO.createImageInputStream(o);
            System.out.println("getFileType iis: " + iis);

            // Find all image readers that recognize the image format
            Iterator<ImageReader> iter = ImageIO.getImageReaders(iis);
            if (!iter.hasNext()) {
                // No readers found
                return null;
            }

            // Use the first reader
            ImageReader reader = iter.next();

            // Close stream
            iis.close();

            // Return the format name
            System.out.println("reader.getFormatName()="
                    + reader.getFormatName());
            return reader.getFormatName();
        } catch (IOException e) {
            //
        }

        // The image could not be read
        return null;

    }

    /**
     * 根据文件内容及目标路径参数，创建文件
     */
    // public static void createFileByContent(String content, String targetPath)
    // {
    // File f = new File(targetPath);
    // FileWriter fw = null;
    // try {
    // fw = new FileWriter(f);
    // fw.write(content);
    // fw.close();
    // } catch (IOException e) {
    // e.printStackTrace();
    // }
    // }

    /**
     * 创建指定内容的文件，对已有同名文件作覆盖处理
     */
    // public static void makeFile(String targetPath, String content) {
    // File f = new File(targetPath);
    // FileWriter fw = null;
    // try {
    // fw = new FileWriter(f);
    // fw.write(content);
    // fw.close();
    // } catch (IOException e) {
    // e.printStackTrace();
    // }
    // }

    // public static void folderSummarize(String str1) {
    // LinkedList<String> folderList = new LinkedList<String>();
    // folderList.add(str1);
    // while (folderList.size() > 0) {
    // File file = new File(folderList.peek());
    // folderList.removeLast();
    // File[] files = file.listFiles();
    // ArrayList<File> fileList = new ArrayList<File>();
    // for (int i = 0; i < files.length; i++) {
    // if (files[i].isDirectory()) {
    // folderList.add(files[i].getPath());
    // System.out.println(files[i].getParent()+","+files[i].getName()+","+files[i].lastModified());
    // } else {
    // fileList.add(files[i]);
    // System.out.println(files[i].getParent()+","+files[i].getName()+","+files[i].length());
    // }
    // }
    // for (File f : fileList) {
    // File str2=f.getAbsoluteFile();
    // }
    // }
    //
    // }

    // public static String move(String fileName, String oldPath, String
    // newPath) {
    // String flag = "0";
    // File oldfile=new File(oldPath+"/"+fileName);
    // File newfile=new File(newPath+"/"+fileName);
    // if(newfile.exists()) {
    // flag = "1";
    // }
    // else {
    // oldfile.renameTo(newfile);
    // }
    // return flag;
    // }

    public static boolean ifFileExist(String path) {
        boolean flag = false;
        File file = new File(path);
        if (file.exists()) {
            flag = true;
        }
        return flag;
    }

    /**
     * 重命名文件夹或文件
     */
    // public static String rename(String path, String oldName, String newName)
    // {
    // String flag = "0";
    // File oldfile=new File(path+"/"+oldName);
    // File newfile=new File(path+"/"+newName);
    // if(oldfile.exists()) {
    // if(newfile.exists()) {
    // flag = "1";
    // } else {
    // oldfile.renameTo(newfile);
    // }
    // } else {
    // flag = "2";
    // }
    // return flag;
    // }

    /**
     * 重命名文件（夹）
     * 
     * @param oldPath
     *            -旧文件路径, newPath-新文件路径
     * @return
     */

    // public static String rename(String oldPath, String newPath) {
    // String flag = "0";
    // File oldfile=new File(oldPath);
    // File newfile=new File(newPath);
    // if(oldfile.exists()) {
    // if(newfile.exists()) {
    // flag = "1";
    // } else {
    // oldfile.renameTo(newfile);
    // }
    // } else {
    // flag = "2";
    // }
    // return flag;
    // }
    //获取文件字节数组
    public static byte[] getBytes(File file){  
        byte[] buffer = null;  
        try {  
            FileInputStream fis = new FileInputStream(file);  
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);  
            byte[] b = new byte[1000];  
            int n;  
            while ((n = fis.read(b)) != -1) {  
                bos.write(b, 0, n);  
            }  
            fis.close();  
            bos.close();  
            buffer = bos.toByteArray();  
        } catch (FileNotFoundException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        return buffer;  
    }  
    /**
     * 
     * Description：根据文件输入和输出流写入文件
     * @param zfin
     * 			输入流
     * @param zfout
     * 			输出流
     * @return    
     * @return boolean
     * 			true：成功    
     *
     */
    public static boolean writeFile(FileInputStream zfin, FileOutputStream zfout) 
	{
		try {
			byte[] b=new byte[1024];
	        int byteread = 0; 
			while((byteread =zfin.read(b))!=-1)
			{
				  zfout.write(b, 0, byteread); 
			}
			zfout.flush();
			zfout.close();
			zfin.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
    public static String getMimeType(String fileUrl)
    	      throws java.io.IOException
    {
    	FileNameMap fileNameMap = URLConnection.getFileNameMap();
    	String type = fileNameMap.getContentTypeFor(fileUrl);
    	return type;
    }
    /**
     * 保存文件到本地
     * @param localSavedFolder 本地文件夹
     * @param fileName 文件名
     * @param file 文件
     * @throws IOException
     */
    public static  void  savaFile(String localSavedFolder,String fileName,InputStream file) throws IOException {
		//创建文件夹
    	createFolderByPath(localSavedFolder);
    	FileUtils.copyInputStreamToFile(file, new File(localSavedFolder+File.separator+fileName));
	}
    public static void main(String args[]) throws Exception {
    	File file=new File("F:/05_数据管理小组/原始数据/国产卫星/ZY3");
    	String[] filename=file.list();
    	for (int i = 0; i < filename.length; i++) {
    		System.out.println(filename[i]+"/"+FileTools.getMimeType(filename[i]));
		}
        
        // output :  text/plain
      }
}