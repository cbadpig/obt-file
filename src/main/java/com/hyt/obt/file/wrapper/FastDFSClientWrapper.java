package com.hyt.obt.file.wrapper;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.github.tobato.fastdfs.domain.FileInfo;
import com.github.tobato.fastdfs.domain.MataData;
import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.domain.ThumbImageConfig;
import com.github.tobato.fastdfs.proto.storage.DownloadCallback;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.hyt.obt.file.config.fastdfs.DownloadCallbankStream;

/**
 * 文件服务客户端包装类
 * @author liuq
 *
 */
@Component
public class FastDFSClientWrapper {
	/**
	 * Http连接的超时时间
	 */
	private static final int CONNECTION_TIME_OUT = 6000;
	/**
	 * Http 读取超时时间
	 */
	private static final int READ_TIME_OUT = 15000;
	/**
	 * 存储进制数
	 */
	private static final int MEMORY_NUM = 1024;
	/**
	 * 流读取结束标志数
	 */
	private static final int STREAM_END_FLAG = -1;
	/**
	 * 请求成功状态码
	 */
	private static final int REQUEST_SUCCESS_CODE = 200;
	@Autowired
	private FastFileStorageClient storageClient;

	@Autowired
	private ThumbImageConfig thumbImageConfig;

	/**
	 * @Description: 上传文件
	 * @param file 文件对象
	 * @return String 文件路径
	 * @throws IOException IO异常
	 */
	public String uploadFile(MultipartFile file) throws IOException {
		StorePath storePath = storageClient.uploadFile(file.getInputStream(), file.getSize(),
				FilenameUtils.getExtension(file.getOriginalFilename()), null);
		return storePath.getFullPath();
	}

	/**
	 * @Description: 上传文件
	 * @param bytes  文件数据
	 * @param format 文件格式（后缀）
	 * @return String 文件路径
	 */
	public String uploadFile(byte[] bytes, String format) {
		StorePath storePath = storageClient.uploadFile(new ByteArrayInputStream(bytes), 
				bytes.length, format, null);
		return storePath.getFullPath();
	}

	/**
	 * @Description: 上传文件
	 * @param file 文件对象
	 * @return String 文件路径
	 * @throws IOException IO异常
	 */
	public String uploadFile(File file) throws IOException {
		StorePath storePath = storageClient.uploadFile(FileUtils.openInputStream(file), file.length(),
				FilenameUtils.getExtension(file.getName()), null);
		return storePath.getFullPath();
	}

	/**
	 * @Description: 把字符串作为指定格式的文件上传
	 * @param content 字符串
	 * @param fileExtension 文件格式
	 * @return String 文件路径
	 */
	public String uploadFile(String content, String fileExtension) {
		byte[] buff = content.getBytes(Charset.forName("UTF-8"));
		ByteArrayInputStream stream = new ByteArrayInputStream(buff);
		StorePath storePath = storageClient.uploadFile(stream, buff.length, fileExtension, null);
		return storePath.getFullPath();
	}
	/**
	 * @param fileUrl 下载文件地址
	 * @param fileSize 文件大小
	 * @param fileExtension 文件扩展名
	 * @throws IOException 
	 * @return 返回上传成功后的保存地址
	 */
	public String uploadFileByUrl(String fileUrl, long fileSize, String fileExtension) throws IOException {
		HttpURLConnection connection = null;
        InputStream inputStream = null;
        FileOutputStream out = null;
        try {
            URL url = new URL(fileUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(CONNECTION_TIME_OUT);
            connection.setReadTimeout(READ_TIME_OUT);
            connection.connect();
            if (connection.getResponseCode() == REQUEST_SUCCESS_CODE) {
            	String fileName = UUID.randomUUID().toString().replace("-", "");
            	inputStream = connection.getInputStream();
            	byte[] buffer = new byte[MEMORY_NUM * MEMORY_NUM];
            	String target = "ms" + File.separator + "down" + File.separator;
            	File dir = new File(target);
            	if (!dir.isDirectory()) {
            		dir.mkdirs();
            	}
            	File file = new File(target, fileName + fileExtension);
            	out = new FileOutputStream(file);
            	int b = 0;
        		while ((b = inputStream.read(buffer)) != STREAM_END_FLAG) {
        			out.write(buffer, 0, b);
        		}
            	out.flush();
                StorePath path = storageClient.uploadFile(new FileInputStream(file), fileSize, fileExtension, null);
    			return path.getFullPath();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        	if (inputStream != null) {
        		inputStream.close();
        	}
        	if (out != null) {
        		out.close();
        	}
        	if (connection != null) {
            	connection.disconnect(); // 关闭远程连接
            }
        }
        return null;
	}
	
	/**
	 * @Description: 上传文件并生成缩略图
	 * @param file 文件对象
	 * @return String 文件路径
	 * @throws IOException IO异常
	 */
	public String uploadImageAndCrtThumbImage(MultipartFile file) throws IOException {
		StorePath storePath = storageClient.uploadImageAndCrtThumbImage(file.getInputStream(), file.getSize(),
				FilenameUtils.getExtension(file.getOriginalFilename()), null);
		return storePath.getFullPath();
	}

	/**
	 * @Description: 根据图片路径获取缩略图路径（使用uploadImageAndCrtThumbImage方法上传图片）
	 * @param filePath 图片路径
	 * @return String 缩略图路径
	 */
	public String getThumbImagePath(String filePath) {
		return thumbImageConfig.getThumbImagePath(filePath);
	}

	/**
	 * @Description: 获取元数据
	 * @param filePath 文件路径
	 * @return Set<MataData> 元数据
	 */
	public Set<MataData> getMetadata(String filePath) {
		StorePath storePath = StorePath.praseFromUrl(filePath);
		return storageClient.getMetadata(storePath.getGroup(), storePath.getPath());
	}

	/**
	 * @Description: 获取文件信息
	 * @param filePath 文件路径
	 * @return FileInfo 文件信息
	 */
	public FileInfo queryFileInfo(String filePath) {
		StorePath storePath = StorePath.praseFromUrl(filePath);
		return storageClient.queryFileInfo(storePath.getGroup(), storePath.getPath());
	}

	/**
	 * @Description: 根据文件路径下载文件
	 * @param filePath 文件路径
	 * @return 文件字节数据
	 * @throws IOException IO异常
	 */
	public byte[] downFile(String filePath) throws IOException {
		StorePath storePath = StorePath.praseFromUrl(filePath);
		return storageClient.downloadFile(storePath.getGroup(), 
				storePath.getPath(), new DownloadCallback<byte[]>() {
			@Override
			public byte[] recv(InputStream ins) throws IOException {
				return org.apache.commons.io.IOUtils.toByteArray(ins);
			}
		});
	}
	/**
	 * @Description: 根据文件路径下载文件
	 * @param filePath 文件路径
	 * @return 流数据
	 * @throws IOException IO异常
	 */
	public InputStream downFileByStream(String filePath) throws IOException {
		StorePath storePath = StorePath.praseFromUrl(filePath);
		return storageClient.downloadFile(storePath.getGroup(), 
				storePath.getPath(), new DownloadCallbankStream() {
			@Override
		    public InputStream recv(InputStream ins) throws IOException {
		        return ins;
		    }
		});
	}
	/**
	 * @Description: 根据文件地址删除文件
	 * @param filePath 文件访问地址
	 */
	public void deleteFile(String filePath) {
		StorePath storePath = StorePath.praseFromUrl(filePath);
		storageClient.deleteFile(storePath.getGroup(), storePath.getPath());
	}
}
