package com.cloudbox.mgmt.listener;



import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

import com.chinarsgeo.rscloudmart.web.dao.entity.DownloadRecord;
import com.cloudbox.mgmt.service.DownloadRecordService;
import com.cloudbox.mgmt.service.ImageDataReceiveService;
import com.cloudbox.utils.Base64;
import com.cloudbox.utils.RSAEncrypt;
import com.cloudbox.utils.StringTools;

/**
 * 启动监听器
 * @author wugq
 */
@Service
public class StartupListener implements ApplicationListener<ContextRefreshedEvent> {
	@Autowired
	protected DownloadRecordService downloadRecordService;
	@Autowired
	protected ImageDataReceiveService imageDataReceiveService;
	protected static String groupUserName;
	protected static String groupUserPassword;
	
	@Value("#{bboxSystemProperties[startUseDateStr]}")
	private String startUseDateStr;
	/**
	 * @Description 初始化用户名、密码。
	 * 
	 **/
	static {
		String file = "Rsmartgtcloudos";
		try {
			groupUserName = new String(RSAEncrypt.decrypt(RSAEncrypt.loadPublicKeyByStrDefault(), Base64.decode(StringTools.getValueFromProperties(file, "groupUserName"))));
			groupUserPassword = new String(RSAEncrypt.decrypt(RSAEncrypt.loadPublicKeyByStrDefault(), Base64.decode(StringTools.getValueFromProperties(file, "groupUserPassword"))));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if (event.getApplicationContext().getParent() == null) {
			List<Map<String, Object>> list=downloadRecordService.getDownloadrecordGroupByName(0);//把正在下载的中的数据继续下载
			if(list!=null&&list.size()>0){
				for (Map<String, Object> mapTemp : list) {
					if(mapTemp.get("id")!=null){
						String id=mapTemp.get("id").toString();
						DownloadRecord downloadRecord =downloadRecordService.queryRecordStutasByRecordid(id);
						downloadRecord.setId(id);
						imageDataReceiveService.addWorkThreadQueue(downloadRecord);
					}
				}
			}
        }
		if(StringUtils.isBlank(startUseDateStr)){//首次安装云盒
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			String path=StartupListener.class.getClassLoader().getResource("install.properties").getPath();
			BufferedWriter write = null;
			try {
				write = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(path))));
				write.write("#云盒客户端安装时间\n");
				write.write("startUseDateStr="+sdf.format(new Date()));
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				if(write!=null){
					try {
						write.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			
		}
		
	}
	
}
