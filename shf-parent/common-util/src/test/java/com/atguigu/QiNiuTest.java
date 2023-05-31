package com.atguigu;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.junit.Test;

public class QiNiuTest {

    // 删除空间中的文件
    @Test
    public void deleteFile() {
        //构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(Zone.zone1());
        //...其他参数参考类注释
        String accessKey = "s3BstIh4hbZuzbXyV_Bk4CwnsqvoBqIA2jygG1dB";
        String secretKey = "c9C808F2GrLxdvXkVtYZpCpyL_emu8uP9MXQbB7E";
        String bucket = "asuna21";//空间
        String key = "Fmw0VGvKiPkU3jiU4ETVIavZWzOW";//文件名称
        Auth auth = Auth.create(accessKey, secretKey);
        BucketManager bucketManager = new BucketManager(auth, cfg);
        try {
            bucketManager.delete(bucket, key);
        } catch (QiniuException ex) {
            //如果遇到异常，说明删除失败
            System.err.println(ex.code());
            System.err.println(ex.response.toString());
        }
    }

        // 上传本地文件
        @Test
        public void uploadFile () {
            //构造一个带指定Zone对象的配置类
            //表示服务器在什么位置
            Configuration cfg = new Configuration(Zone.zone1());
            //...其他参数参考类注释
            UploadManager uploadManager = new UploadManager(cfg);
            //...生成上传凭证，然后准备上传
            String accessKey = "s3BstIh4hbZuzbXyV_Bk4CwnsqvoBqIA2jygG1dB";
            String secretKey = "c9C808F2GrLxdvXkVtYZpCpyL_emu8uP9MXQbB7E";
            String bucket = "asuna21";
            //如果是Windows情况下，格式是 D:\\qiniu\\test.png，可支持中文
            String localFilePath = "D:\\安装包\\壁纸\\1\\1.bmp";
            //默认不指定key的情况下，以文件内容的hash值作为文件名
            String key = null;
            //上传图片需要校验
            Auth auth = Auth.create(accessKey, secretKey);
            String upToken = auth.uploadToken(bucket);
            try {
                Response response = uploadManager.put(localFilePath, key, upToken);
                //解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                //打印图片名称
                System.out.println(putRet.key);
                System.out.println(putRet.hash);
            } catch (QiniuException ex) {
                Response r = ex.response;
                System.err.println(r.toString());
                try {
                    System.err.println(r.bodyString());
                } catch (QiniuException e) {
                    e.printStackTrace();
                }
            }
        }
    }
