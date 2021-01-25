import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

import javax.sound.sampled.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalTime;

public class Main {
    public static void main(String[] args) throws LineUnavailableException, InterruptedException, IOException {
        Mixer.Info[] mixerInfos = AudioSystem.getMixerInfo();
        for (Mixer.Info mixerInfo : mixerInfos) {
            Mixer mixer = AudioSystem.getMixer(mixerInfo);

            if (mixer.getMaxLines(Port.Info.MICROPHONE) > 0) {
                System.out.println("-------------");
                System.out.println(mixerInfo);
                Port line = (Port) mixer.getLine(Port.Info.MICROPHONE);
                line.open();
                int counter=1;
                //正常发正念钟声4次就设置成counter<5
                while(counter<11){
                    BooleanControl bc = (BooleanControl) line.getControl(BooleanControl.Type.MUTE);
                    bc.setValue(false);
                    //文件存在哪里就设置什么路径
                    String mp3 = "resource/fzn3s.wav";
//                    String mp3 = "/Users/Nora/code/java/fzn-web/src/main/resources/static/fzn3s.wav";
                    InputStream in = new FileInputStream(mp3);
                    AudioStream audioStream = new AudioStream(in);
                    System.out.println("alarm run "+ counter+" time at "+LocalTime.now());
                    AudioPlayer.player.start(audioStream);
                    Thread.sleep(1000 *3);
                   //关闭系统麦克风
                    bc.setValue(true);
                    Thread.sleep(1000 * 60 *5-3170);
                    //测试用 5 秒钟声响一次
//                    Thread.sleep(1000 *2);
                    counter++;
                }

                BooleanControl bc = (BooleanControl) line.getControl(BooleanControl.Type.MUTE);
                if (bc != null) {
                    //将麦克风开启
                    bc.setValue(false); // true to mute the line, false to unmute
                }
                //打印控制参数信息
                Control[] controls = line.getControls();
                for (Control control : controls) {
                    if (control instanceof CompoundControl) {
                        Control[] subControls = ((CompoundControl) control).getMemberControls();
                        for (Control subControl : subControls) {
                            System.out.println(subControl);
                        }
                    } else {
                        System.out.println(control);
                    }
                }
                System.out.println("-----------");
            }
        }
    }
}
