package view;


import com.database.DatabaseManager;
import com.server.NIOServer1;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainFrame
  extends JFrame
{
  private static final Logger logger = LoggerFactory.getLogger(MainFrame.class);
  private static final long serialVersionUID = 1L;
  public static NIOServer1 t = new NIOServer1();
  private static final int DEFAULT_WIDTH = 700;
  private static final int DEFAULT_HEIGHT = 500;
  
  public MainFrame(String title)
  {
    setSize(700, 500);
    setTitle(title);
    Toolkit kit = Toolkit.getDefaultToolkit();
    Dimension screenSize = kit.getScreenSize();
    int screenWidthpx = screenSize.width;
    int screenHeightpx = screenSize.height;
    setLocation(screenWidthpx / 3, screenHeightpx / 3);
    setLocationByPlatform(false);

    MainPannel loginPanel = new MainPannel();
    setContentPane(loginPanel);
    
    setDefaultCloseOperation(0);
    addWindowListener(new WindowAdapter()
    {
      public void windowClosing(WindowEvent e)
      {
        int flag = JOptionPane.showConfirmDialog(null, "关闭后将停止接受数据，请谨慎操作。", 
          "Care!", 0, 
          1);
        if (flag == 0)
        {
          System.exit(0);
          MainFrame.logger.info("关闭了服务器");
        }
        else {}
      }
    });
    setVisible(true);
    setResizable(true);
  }
  
  private class MainPannel
    extends JPanel
  {
    private static final long serialVersionUID = 1L;
    JPanel infoPanel;
    JLabel label;
    
    public MainPannel()
    {
      this.infoPanel = new JPanel();
      this.label = new JLabel();
      this.label.setText("服务器运行中。。。请不要关闭此窗口");
      setLayout(new BorderLayout());
      

      this.infoPanel.setLayout(new FlowLayout(1));
      this.infoPanel.add(label);
      
      add(this.infoPanel, "Center");
    }
    
  }
  
  public static void main(String[] args)
  {
    new MainFrame("服务器");
	//开启自动创建和删除数据库表
	new DatabaseManager();
	//开启接收服务器
	try
     {
       t.startServer();
     }
     catch (Exception e)
     {
       e.printStackTrace();
     }
  }
}
