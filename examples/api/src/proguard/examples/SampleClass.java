package proguard.examples;

/**
 * This is a sample class that can be processed.
 */
public class SampleClass
{
    public static void main(String[] args)
    {
        System.out.println("The answer is " + getAnswer());
    }


    private static int getAnswer()
    {
        int answer = 0;

        for (int i = 0; i < 3; i++)
        {
            answer += 7;
        }

        return 2 * answer;
    }
}
