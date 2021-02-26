package helpQuenue.admin;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;
import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.UIManager;




public class AdminGui extends JFrame {

    JLabel lblNewLabel;
    JButton cancelBtn;
    JTextField textField_WorkStationName;
    JButton clearBtn;
    JLabel workStationName;
    JTextArea textArea;
    JLabel lblCoursemanagement;
    JLabel lblCourse;
    JTextField textField_CourseNo;
    JLabel lblSection;
    JTextField textField_Section;
    JLabel lblStartDate;
    JTextField textField_StartDate;
    JLabel lblEndDate;
    JTextField textField_EndDate;
    JComboBox<String> comboBox_DayOfWeek;
    JLabel lblDayOfWeek;
    JLabel lblStartTime;
    JTextField textField_StartTime;
    JLabel lblEndTime;
    JTextField textField_EndTime;
    JButton addBtn;
    JButton deleteBtn;
    JLabel lblCourseId;
    JTextField textField_CourseID;
    JButton refreshBtn;





public AdminGui() {
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setTitle("P115 Lab Help Queue - Administrator");
    setSize(1500, 800);
    getContentPane().setLayout(null);

    lblNewLabel = new JLabel("P115 Lab - Help Queue Administrator");
    lblNewLabel.setBounds(320, 10, 541, 30);
    lblNewLabel.setForeground(Color.BLUE);
    lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
    lblNewLabel.setFont(new Font("Consolas", Font.BOLD, 25));

    workStationName = new JLabel("WorkStation Name:");
    workStationName.setBounds(230, 115, 142, 15);
    workStationName.setFont(new Font("Consolas", Font.PLAIN, 14));

    textField_WorkStationName = new JTextField();
    textField_WorkStationName.setBounds(382, 112, 128, 21);
    textField_WorkStationName.setFont(new Font("Consolas", Font.PLAIN, 14));
    textField_WorkStationName.setColumns(10);

    cancelBtn = new JButton("Cancel");
    cancelBtn.setBounds(520, 111, 93, 23);
    cancelBtn.setFont(new Font("Consolas", Font.PLAIN, 14));

    clearBtn = new JButton("Clear Current Help Queue");
    clearBtn.setBounds(651, 111, 245, 23);
    clearBtn.setFont(new Font("Consolas", Font.PLAIN, 14));

    /* show result*/
    textArea = new JTextArea();
    textArea.setFont(new Font("Consolas", Font.PLAIN, 18));
    textArea.setBounds(650, 180, 800, 480);


    lblCoursemanagement = new JLabel("Course Management");
    lblCoursemanagement.setHorizontalAlignment(SwingConstants.LEFT);
    lblCoursemanagement.setForeground(Color.BLACK);
    lblCoursemanagement.setFont(new Font("Consolas", Font.PLAIN, 23));
    lblCoursemanagement.setBounds(10, 150, 319, 30);


    lblCourse = new JLabel("Course:");
    lblCourse.setBounds(80, 211, 63, 15);
    lblCourse.setFont(new Font("Consolas", Font.PLAIN, 14));

    textField_CourseNo = new JTextField();
    textField_CourseNo.setBounds(187, 207, 120, 23);
    textField_CourseNo.setFont(new Font("Consolas", Font.PLAIN, 14));
    textField_CourseNo.setColumns(10);

    lblSection = new JLabel("Section:");
    lblSection.setBounds(331, 211, 71, 15);
    lblSection.setFont(new Font("Consolas", Font.PLAIN, 14));

    textField_Section = new JTextField();
    textField_Section.setBounds(438, 207, 120, 23);
    textField_Section.setFont(new Font("Consolas", Font.PLAIN, 14));
    textField_Section.setColumns(10);

    lblStartDate = new JLabel("Start Date:");
    lblStartDate.setBounds(80, 246, 97, 15);
    lblStartDate.setFont(new Font("Consolas", Font.PLAIN, 14));

    textField_StartDate = new JTextField();
    textField_StartDate.setBounds(187, 241, 120, 23);
    textField_StartDate.setFont(new Font("Consolas", Font.PLAIN, 14));
    textField_StartDate.setColumns(10);

    lblEndDate = new JLabel("End Date:");
    lblEndDate.setBounds(331, 245, 97, 15);
    lblEndDate.setFont(new Font("Consolas", Font.PLAIN, 14));

    textField_EndDate = new JTextField();
    textField_EndDate.setBounds(438, 241, 120, 23);
    textField_EndDate.setFont(new Font("Consolas", Font.PLAIN, 14));
    textField_EndDate.setColumns(10);

    lblDayOfWeek = new JLabel("Day of Week:");
    lblDayOfWeek.setBounds(80, 281, 118, 15);
    lblDayOfWeek.setFont(new Font("Consolas", Font.PLAIN, 14));

    comboBox_DayOfWeek = new JComboBox<String>();
    comboBox_DayOfWeek.setBounds(187, 277, 120, 23);
    comboBox_DayOfWeek.setFont(new Font("Consolas", Font.PLAIN, 14));
    comboBox_DayOfWeek.setModel(new DefaultComboBoxModel<String>(
            new String[] { "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday" }));

    lblStartTime = new JLabel("Start Time:");
    lblStartTime.setBounds(80, 316, 97, 15);
    lblStartTime.setFont(new Font("Consolas", Font.PLAIN, 14));

    textField_StartTime = new JTextField();
    textField_StartTime.setBounds(187, 312, 120, 23);
    textField_StartTime.setFont(new Font("Consolas", Font.PLAIN, 14));
    textField_StartTime.setColumns(10);

    lblEndTime = new JLabel("End Time:");
    lblEndTime.setBounds(331, 316, 97, 15);
    lblEndTime.setFont(new Font("Consolas", Font.PLAIN, 14));

    textField_EndTime = new JTextField();
    textField_EndTime.setBounds(438, 312, 120, 23);
    textField_EndTime.setFont(new Font("Consolas", Font.PLAIN, 14));
    textField_EndTime.setColumns(10);


    addBtn = new JButton("Add");
    addBtn.setFont(new Font("Consolas", Font.PLAIN, 14));
    addBtn.setBounds(269, 354, 142, 25);


    lblCourseId = new JLabel("Course ID:");
    lblCourseId.setBounds(80, 408, 97, 17);
    lblCourseId.setFont(new Font("Consolas", Font.PLAIN, 14));

    textField_CourseID = new JTextField();
    textField_CourseID.setBounds(187, 405, 253, 23);
    textField_CourseID.setFont(new Font("Consolas", Font.PLAIN, 14));
    textField_CourseID.setColumns(10);

    deleteBtn = new JButton("Delete");
    deleteBtn.setBounds(450, 404, 103, 25);
    deleteBtn.setFont(new Font("Consolas", Font.PLAIN, 14));

    refreshBtn = new JButton("View Courses");
    refreshBtn.setFont(new Font("Consolas", Font.PLAIN, 14));
    refreshBtn.setBounds(269, 454, 142, 25);




    getContentPane().add(lblNewLabel);
    getContentPane().add(workStationName);
    getContentPane().add(textField_WorkStationName);
    getContentPane().add(cancelBtn);
    getContentPane().add(clearBtn);
    getContentPane().add(textArea);
    getContentPane().add(lblCoursemanagement);
    getContentPane().add(lblCourse);
    getContentPane().add(textField_CourseNo);
    getContentPane().add(lblSection);
    getContentPane().add(textField_Section);
    getContentPane().add(lblStartDate);
    getContentPane().add(textField_StartDate);
    getContentPane().add(lblEndDate);
    getContentPane().add(textField_EndDate);
    getContentPane().add(lblDayOfWeek);
    getContentPane().add(comboBox_DayOfWeek);
    getContentPane().add(lblStartTime);
    getContentPane().add(textField_StartTime);
    getContentPane().add(lblEndTime);
    getContentPane().add(textField_EndTime);
    getContentPane().add(addBtn);
    getContentPane().add(lblCourseId);
    getContentPane().add(textField_CourseID);
    getContentPane().add(deleteBtn);
    getContentPane().add(refreshBtn);








}
}
