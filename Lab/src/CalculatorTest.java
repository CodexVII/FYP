import javax.swing.JFrame;

/**
 * Displays the calculator.
 * 
 * @author Ian Lodovica (13131567)
 * @version 12/03/14 (Lab07)
 */
public class CalculatorTest
{
    public static void main (String[] args )
    {
        Calculator calc = new Calculator();
        calc.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        calc.setSize( 500, 300 );
        calc.setVisible( true );
    }
}