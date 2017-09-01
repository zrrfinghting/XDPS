package com.xdps.logic.util;

import net.sf.json.JSONObject;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * 上传文件
 *
 * @autherAdmin Deram Zhao
 * @creat 2017/8/30
 */
public class UploadFileUtil {

    public static Properties propers = ReadProperties.getPropes("/application.properties");//加载properties文件
    public static String uploadFilePath = propers.getProperty("uploadFilePath");//文件保存路径
    public static int uploadFileSize = 5 * 1024 * 1024;//文件大小设置

    public static String singleFileUpload(MultipartFile file) {
        String fileName;
        if (file.isEmpty())
            return JsonUtil.returnStr(JsonUtil.FAIL, "请选择一个文件");
        if (!scopeFileSize(file))
            return JsonUtil.returnStr(JsonUtil.FAIL, "文件太大");
        if (".exe".equals(getFileType(file.getOriginalFilename())))
            return JsonUtil.returnStr(JsonUtil.FAIL, "不能上传exe文件");
        try {

            byte[] bytes = file.getBytes();//获取文件、保存文件
            fileName = System.currentTimeMillis() + getFileType(file.getOriginalFilename()); //用时间戳+文件名重命名文件名
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(new File(uploadFilePath + "/" + fileName)));
            out.write(bytes);
            out.flush();
            out.close();
            JSONObject json = new JSONObject();
            json.put("status",JsonUtil.SUCCESS);
            json.put("msg","上传成功");
            json.put("fileName",file.getOriginalFilename());
            return json.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return JsonUtil.returnStr(JsonUtil.FAIL, "文件上传失败");
        }
    }


    /**
     * 设置文件大小
     *
     * @param file
     * @return
     */
    public static boolean scopeFileSize(MultipartFile file) {
        if (propers.getProperty("uploadFileSize") != null && !propers.getProperty("uploadFileSize").equals(""))
            uploadFileSize = Integer.parseInt(propers.getProperty("uploadFileSize")) * 1024 * 1024;//单位为：字节
        if (file.getSize() > 0 && file.getSize() < uploadFileSize) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取文件名后缀
     *
     * @param fileName
     * @return
     */
    public static String getFileType(String fileName) {
        if (!"".equals(fileName) && fileName != null && fileName.indexOf(".") >= 0) {
            return fileName.substring(fileName.lastIndexOf("."), fileName.length());
        }
        return "";
    }
}
