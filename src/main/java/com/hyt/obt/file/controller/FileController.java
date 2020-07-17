package com.hyt.obt.file.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.github.tobato.fastdfs.domain.FileInfo;
import com.github.tobato.fastdfs.domain.MataData;
import com.hyt.obt.file.wrapper.FastDFSClientWrapper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 文件服务器接口
 * @author liuq
 *
 */
@Api(tags = "文件服务器接口")
@RestController
@RequestMapping("/file")
public class FileController {
	
	@Autowired
	private FastDFSClientWrapper fastDFSClientWrapper;

	/**
	 * 上传MultipartFile
	 * @param file 文件对象
	 * @return String 文件路径
	 * @throws IOException IO异常
	 */
	@ApiOperation(value = "上传MultipartFile", notes = "返回文件路径")
	@RequestMapping(value = "/upload/multipartfile", method = RequestMethod.POST, 
		consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public String uploadFile(@RequestPart(value = "file") MultipartFile file) throws IOException {
		return fastDFSClientWrapper.uploadFile(file);
	}
	
	/**
	 * @Description: 上传文件
	 * @param bytes  文件数据
	 * @param format 文件格式（后缀）
	 * @return String 文件路径
	 */
	@ApiOperation(value = "上传byte[]", notes = "format是文件格式（后缀），返回文件路径")
	@RequestMapping(value = "/upload/bytes", method = RequestMethod.POST)
	public String uploadFile(@RequestBody byte[] bytes, @RequestParam String format) {
		return fastDFSClientWrapper.uploadFile(bytes, format);
	}
	
	/**
	 * @Description: 上传文件
	 * @param file 文件对象
	 * @return String 文件路径
	 * @throws IOException IO异常
	 */
	@ApiOperation(value = "上传File", notes = "返回文件路径")
	@RequestMapping(value = "/upload/file", method = RequestMethod.POST)
	public String uploadFile(@RequestBody File file) throws IOException {
		return fastDFSClientWrapper.uploadFile(file);
	}
 
	/**
	 * @Description: 把字符串作为指定格式的文件上传
	 * @param content 字符串
	 * @param fileExtension 文件格式
	 * @return String 文件路径
	 */
	@ApiOperation(value = "把字符串作为指定格式的文件上传", notes = "返回文件路径")
	@RequestMapping(value = "/upload/string", method = RequestMethod.POST)
	public String uploadFile(String content, String fileExtension) {
		return fastDFSClientWrapper.uploadFile(content, fileExtension);
	}
	/**
	 * @param url 腾讯视频地址
	 * @param fileSize 视频大小
	 * @param fileExtension 文件扩展名
	 * @throws IOException 
	 * @return 返回上传成功后保存路径
	 */
	@ApiOperation(value = "根据url上传文件", notes = "返回文件路径")
	@RequestMapping(value = "/upload/url", method = RequestMethod.POST)
	public String uploadFileByUrl(String url, long fileSize, String fileExtension) {
		try {
			return fastDFSClientWrapper.uploadFileByUrl(url, fileSize, fileExtension);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * @Description: 上传文件并生成缩略图
	 * @param file 文件对象
	 * @return String 文件路径
	 * @throws IOException IO异常
	 */
	@ApiOperation(value = "上传图片并生成缩略图", notes = "返回文件路径")
	@RequestMapping(value = "/upload/image/crtthumbimage", method = RequestMethod.POST, 
		consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public String uploadImageAndCrtThumbImage(@RequestPart(value = "file") MultipartFile file) throws IOException {
		return fastDFSClientWrapper.uploadImageAndCrtThumbImage(file);
	}
	
	/**
	 * @Description: 根据图片路径获取缩略图路径（使用uploadImageAndCrtThumbImage方法上传图片）
	 * @param filePath 图片路径
	 * @return String 缩略图路径
	 */
	@ApiOperation(value = "根据图片路径获取缩略图路径", notes = "返回缩略图路径")
	@RequestMapping(value = "/get/thumbimagepath", method = RequestMethod.GET)
	public String getThumbImagePath(String filePath) {
		return fastDFSClientWrapper.getThumbImagePath(filePath);
	}
	
	/**
	 * @Description: 获取元数据
	 * @param filePath 文件路径
	 * @return Set<MataData> 元数据
	 */
	@ApiOperation(value = "获取元数据", notes = "返回元数据")
	@RequestMapping(value = "/get/metadata", method = RequestMethod.GET)
	public Set<MataData> getMetadata(String filePath) {
		try {
			return  fastDFSClientWrapper.getMetadata(filePath);
		} catch (Exception e) {
			return  null;
		}
	}
	
	/**
	 * @Description: 获取文件信息
	 * @param filePath 文件路径
	 * @return FileInfo 文件信息
	 */
	@ApiOperation(value = "获取文件信息", notes = "返回文件信息")
	@RequestMapping(value = "/query/fileinfo", method = RequestMethod.GET)
	public FileInfo queryFileInfo(String filePath) {
		try {
			return  fastDFSClientWrapper.queryFileInfo(filePath);
		} catch (Exception e) {
			return  null;
		}
	}
	
	/**
	 * @Description: 根据文件路径下载文件
	 * @param filePath 文件路径
	 * @return 文件字节数据
	 * @throws IOException IO异常
	 */
	@ApiOperation(value = "根据文件路径下载文件", notes = "返回文件字节数据")
	@RequestMapping(value = "/down", method = RequestMethod.GET)
	public byte[] downFile(String filePath) throws IOException {
		try {
			return fastDFSClientWrapper.downFile(filePath);
		} catch (Exception e) {
			return null;
		}
	}
	/**
	 * @Description: 根据文件路径下载文件
	 * @param filePath 文件路径
	 * @return 流数据
	 * @throws IOException IO异常
	 */
	@ApiOperation(value = "根据文件路径下载文件", notes = "返回文件字节数据")
	@RequestMapping(value = "/downFileByStream", method = RequestMethod.GET)
	public InputStream downFileByStream(String filePath) throws IOException {
		try {
			return fastDFSClientWrapper.downFileByStream(filePath);
		} catch (Exception e) {
			return null;
		}
	}
	/**
	 * @Description: 根据文件地址删除文件
	 * @param filePath 文件访问地址
	 */
	@ApiOperation(value = "根据文件路径删除文件", notes = "添加考生基本信息")
	@RequestMapping(value = "/delete", method = RequestMethod.DELETE)
	public void deleteFile(String filePath) {
		try {
			fastDFSClientWrapper.deleteFile(filePath);
		} catch (Exception e) {
			
		}
	}

}
