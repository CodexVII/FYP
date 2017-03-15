import java.awt.GridLayout;
import java.awt.BorderLayout;
import javax.swing.JTextField;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;
/**
 * A simple calculator GUI. Its basic components are JButton and JTextField. 
 * The calculator is organized by three JPanels and the use of BorderLayout. 
 * There is a functional clear button that clears all inputs. 
 * Made the decision to not put "=" on textField as with normal calculators.
 * 
 * @author Ian Lodovica (13131567)
 * @version 12/03/14 (Lab07)
 */
public class Calculator extends JFrame
{
    private JTextField display;
    private JButton[] buttons;
    private JButton equals;
    private JButton[] func;

    private GridLayout centerLayout;
    private GridLayout rightLayout;
    private GridLayout leftLayout;

    private JPanel mainPanel;
    private JPanel rightPanel;
    private JPanel topPanel;
    private JPanel leftPanel;

    private static final String[] names1 =
        { "7", "8", "9", "/", 
            "4", "5", "6", "*", 
            "1", "2", "3", "-", 
            "C", "0", ".", "+"};
    private static final String name2 = "=";
    private static final String[] functions =
        { "sin", "(", "tan", ")",
            "cos", "^", "log", "e" };
    /**
     * Builds the calculator
     */        
    public Calculator()
    {
        super( "Calculator" );
        //Creates new grid layouts
        centerLayout = new GridLayout( 4, 4, 5, 5); //4 by 4 with gaps of 5 px
        rightLayout = new GridLayout(1, 1, 5, 5);
        leftLayout = new GridLayout(4, 2, 5, 5);

        //Creates new panels
        topPanel = new JPanel();
        mainPanel = new JPanel(centerLayout);
        rightPanel = new JPanel(rightLayout);
        leftPanel = new JPanel(leftLayout);

        //Text Area. At NORTH
        display = new JTextField(20);
        display.setHorizontalAlignment(JTextField.RIGHT);

        //Main set of buttons. At CENTER
        buttons = new JButton [names1.length];
        mainPanel.setLayout(centerLayout);
        //Adds buttons to the center panel as well as registering listeners to each button.
        for ( int i = 0; i < names1.length; i++ )
        {
            buttons[i] = new JButton( names1[i] );
            buttons[i].addActionListener(new Input());
            if( i != 3 && i != 7 && i != 11 && i != 15)
            {
                buttons[i].setBackground(Color.white);
            }
            else buttons[i].setBackground(Color.lightGray);

            mainPanel.add(buttons[i]); 
        }

        //Equals sign. At SOUTH
        rightPanel.setLayout( rightLayout );
        equals = new JButton(name2); // = sign
        rightPanel.add(equals); //add button to JPanel
        equals.setBackground(Color.lightGray);

        //Set of functions placed at west
        func = new JButton [ functions.length ];
        leftPanel.setLayout(leftLayout);
        //Adds buttons to left panel and registering listeners
        for ( int j = 0; j < functions.length; j++ )
        {
            func[j] = new JButton( functions[j] );
            func[j].addActionListener ( new Input() );
            func[j].setBackground(Color.lightGray);
            leftPanel.add(func[j]);
        }

        //Adds panels to JFrame
        add(display, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        add(rightPanel, BorderLayout.EAST);
        add(leftPanel, BorderLayout.WEST);
    }

    /**
     * Displays all inputs from the buttons to the textField
     */
    public class Input implements ActionListener
    {
        public void actionPerformed( ActionEvent e)
        {
            //Clears text when C is pressed
            if( e.getSource() == buttons[12] )
            {
                display.setText("");
            }

            //Adds input from main button set to TextField. Exception is C
            for (int i = 0; i < names1.length; i++)
            {
                if ( e.getSource() == buttons[i] && e.getSource() != buttons[12] )
                {
                    display.setText(display.getText() + names1[i]);
                }
            }

            //Adds input from function buttons set to TextField.
            for ( int j = 0; j < functions.length; j++ )
            {
                int bracket = 0;
                if (j%2 == 0)
                {
                    bracket = 1;
                }

                if ( e.getSource() == func[j] )
                {
                    display.setText(display.getText() + functions[j]);

                    if( bracket == 1 )
                    {
                        display.setText(display.getText() + "(" );
                    }
                }
            }
        }
    }
}