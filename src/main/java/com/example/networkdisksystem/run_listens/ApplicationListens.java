package com.example.networkdisksystem.run_listens;

import com.example.networkdisksystem.config.HadoopConfig;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

@Component
public class ApplicationListens implements CommandLineRunner, DisposableBean {
    @Autowired
    private HadoopConfig hadoopConfig;

    @Override
    public void run(String... args) throws Exception {
        hadoop("start");
    }
    @Override
    public void destroy() throws Exception {
        hadoop("stop");
    }

    /**
     * hadoop自启和关闭
     * @param s start/stop
     * @throws JSchException
     */
    private void hadoop(String s) throws JSchException {
        // 初始化SSH连接参数
        String username = hadoopConfig.getUsername();
        String password = hadoopConfig.getPassword();

        JSch jsch = new JSch();
        Session session = jsch.getSession(username, hadoopConfig.getServer()[0], 22);
        session.setPassword(password);
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect();


        // 执行脚本命令
        try {
            String scriptPath = hadoopConfig.getScriptPath()[0];
            String command = scriptPath + " "+s;
            ChannelExec channelExec = (ChannelExec) session.openChannel("exec");
            channelExec.setCommand(command);
            channelExec.connect();
            // 读取脚本输出
            InputStream in = channelExec.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            // 关闭连接
            channelExec.disconnect();
            session.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
