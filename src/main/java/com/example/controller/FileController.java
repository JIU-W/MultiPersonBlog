package com.example.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import com.example.common.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;

/**
 * 文件接口
 */
@RestController
@RequestMapping("/files")
public class FileController {

    // 文件上传存储路径
    private static final String filePath = System.getProperty("user.dir") + "/files/";
    //System.getProperty("user.dir"):当前项目根目录
    @Value("${server.port:9090}")
    private String port;

    @Value("${ip:localhost}")
    private String ip;

    /**
     * 文件上传
     */
    @PostMapping("/upload")
    public Result upload(MultipartFile file) {
        String flag;
        /*这是一个同步代码块。括号中的FileController.class是同步锁的对象。
        这意味着，任何尝试进入这个同步代码块的线程都必须首先获得FileController.class对象的锁。
        如果已经有一个线程持有这个锁，并正在执行同步代码块，
        那么其他任何尝试进入这个代码块的线程都将被阻塞，直到第一个线程释放锁。
        这种同步机制可以确保同一时间只有一个线程能够执行同步代码块中的代码，
        从而避免多线程并发访问时可能产生的数据不一致问题。*/
        synchronized (FileController.class) {
            //获取当前的系统时间（以毫秒为单位）
            //生成一个时间戳作为标志
            flag = System.currentTimeMillis() + "";
            //当前线程暂停执行1毫秒
            ThreadUtil.sleep(1L);
        }
        //
        String fileName = file.getOriginalFilename();
        try {
            if (!FileUtil.isDirectory(filePath)) {
                FileUtil.mkdir(filePath);
            }
            // 文件存储形式：时间戳-文件名
            FileUtil.writeBytes(file.getBytes(), filePath + flag + "-" + fileName);
                         //  ***/xm-blog/files/1697438073596-avatar.png
            System.out.println(fileName + "--上传成功");

        } catch (Exception e) {
            System.err.println(fileName + "--文件上传失败");
        }
        String http = "http://" + ip + ":" + port + "/files/";

        return Result.success(http + flag + "-" + fileName);
        // http://localhost:9090/files/1697438073596-avatar.png
        // 返回的url就是“获取文件(文件下载)的接口路径
    }


    /**
     * 获取文件(文件下载)
     *
     * @param flag
     * @param response
     */
    @GetMapping("/{flag}")   //1697438073596-avatar.png
    public void avatarPath(@PathVariable String flag, HttpServletResponse response) {
        OutputStream os;
        try {
            if (StrUtil.isNotEmpty(flag)) {
                response.addHeader("Content-Disposition",
                        "attachment;filename=" + URLEncoder.encode(flag, "UTF-8"));
                response.setContentType("application/octet-stream");
                byte[] bytes = FileUtil.readBytes(filePath + flag);
                os = response.getOutputStream();
                os.write(bytes);
                os.flush();
                os.close();
            }
        } catch (Exception e) {
            System.out.println("文件下载失败");
        }
    }

    /**
     * 删除文件
     *
     * @param flag
     */
    @DeleteMapping("/{flag}")
    public void delFile(@PathVariable String flag) {
        FileUtil.del(filePath + flag);
        System.out.println("删除文件" + flag + "成功");
    }

    /**
     * 富文本上传
     * @param file
     * @return
     */
    @PostMapping("/editor/upload")
    public Dict editorUpload(MultipartFile file) {
        String flag;
        //同步代码块 + 锁
        synchronized (FileController.class) {
            //获取当前的系统时间（以毫秒为单位）
            //生成一个时间戳作为标志
            flag = System.currentTimeMillis() + "";
            //当前线程暂停执行1毫秒
            ThreadUtil.sleep(1L);
        }
        //
        String fileName = file.getOriginalFilename();
        try {
            if (!FileUtil.isDirectory(filePath)) {
                FileUtil.mkdir(filePath);
            }
            // 文件存储形式：时间戳-文件名
            FileUtil.writeBytes(file.getBytes(), filePath + flag + "-" + fileName);
            //  ***/xm-blog/files/1697438073596-avatar.png
            System.out.println(fileName + "--上传成功");

        } catch (Exception e) {
            System.err.println(fileName + "--文件上传失败");
        }
        String http = "http://" + ip + ":" + port + "/files/";

        return Dict.create().set("errno", 0).set("data", CollUtil.newArrayList(Dict.create()
                .set("url", http + flag + "-" + fileName)));
    }

}
