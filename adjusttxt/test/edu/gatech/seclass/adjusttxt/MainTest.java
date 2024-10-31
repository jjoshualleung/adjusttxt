package edu.gatech.seclass.adjusttxt;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.api.io.TempDir;

// DO NOT ALTER THIS CLASS. Use it as an example for MyMainTest.java

@Timeout(value = 1, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
public class MainTest {
    private final String usageStr =
            "Usage: adjusttxt [ -s number | -w spacing | -x | -r target | -p prefix ] FILE"
                    + System.lineSeparator();

    @TempDir Path tempDirectory;

    @RegisterExtension OutputCapture capture = new OutputCapture();

    /* ----------------------------- Test Utilities ----------------------------- */

    /**
     * Returns path of a new "input.txt" file with specified contents written into it. The file will
     * be created using {@link TempDir TempDir}, so it is automatically deleted after test
     * execution.
     *
     * @param contents the text to include in the file
     * @return a Path to the newly written file, or null if there was an issue creating the file
     */
    private Path createFile(String contents) {
        return createFile(contents, "input.txt");
    }

    /**
     * Returns path to newly created file with specified contents written into it. The file will be
     * created using {@link TempDir TempDir}, so it is automatically deleted after test execution.
     *
     * @param contents the text to include in the file
     * @param fileName the desired name for the file to be created
     * @return a Path to the newly written file, or null if there was an issue creating the file
     */
    private Path createFile(String contents, String fileName) {
        Path file = tempDirectory.resolve(fileName);
        try {
            Files.writeString(file, contents);
        } catch (IOException e) {
            return null;
        }

        return file;
    }

    /**
     * Takes the path to some file and returns the contents within.
     *
     * @param file the path to some file
     * @return the contents of the file as a String, or null if there was an issue reading the file
     */
    private String getFileContent(Path file) {
        try {
            return Files.readString(file);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /* ------------------------------- Test Cases ------------------------------- */

    @Test
    public void exampleTest1() {
        String input = "";

        Path inputFile = createFile(input);
        String[] args = {inputFile.toString()};
        Main.main(args);

        Assertions.assertTrue(capture.stdout().isEmpty());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    public void exampleTest2() {
        String input = System.lineSeparator();
        String expected = "";

        Path inputFile = createFile(input);
        String[] args = {"-x", "-p", "some_prefix", inputFile.toString()};
        Main.main(args);

        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    public void exampleTest3() {
        String input = "Hello, world!" + System.lineSeparator()
                + System.lineSeparator()
                + "How are you?" + System.lineSeparator();

        Path inputFile = createFile(input);
        String[] args = {"-s", "2", inputFile.toString()};
        Main.main(args);

        Assertions.assertTrue(capture.stdout().isEmpty());
        Assertions.assertEquals(usageStr, capture.stderr());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    public void exampleTest4() {
        String input = "Okay, here is how this is going to work." + System.lineSeparator()
                + "No shouting!" + System.lineSeparator()
                + "Does that make sense?" + System.lineSeparator()
                + "Alright, good meeting." + System.lineSeparator();
        String expected = "No shouting!" + System.lineSeparator()
                + "Alright, good meeting." + System.lineSeparator();

        Path inputFile = createFile(input);
        String[] args = {"-s", "1", inputFile.toString()};
        Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    public void exampleTest5() {
        String input = "pwd" + System.lineSeparator()
                + "cd Documents" + System.lineSeparator()
                + "echo \"This is a test\" > echoed_sample_text.txt" + System.lineSeparator()
                + "cat echoed_sample_text.txt" + System.lineSeparator()
                + "grep test echoed_sample_text.txt" + System.lineSeparator()
                + "rm echoed_sample_text.txt" + System.lineSeparator();

        String expected = "user@ubuntu:~$pwd" + System.lineSeparator()
                + "user@ubuntu:~$cd Documents" + System.lineSeparator()
                + "user@ubuntu:~$echo \"This is a test\" > echoed_sample_text.txt" + System.lineSeparator()
                + "user@ubuntu:~$cat echoed_sample_text.txt" + System.lineSeparator()
                + "user@ubuntu:~$grep test echoed_sample_text.txt" + System.lineSeparator()
                + "user@ubuntu:~$rm echoed_sample_text.txt" + System.lineSeparator();

        Path inputFile = createFile(input);
        String[] args = {"-p", "user@ubuntu:~$", inputFile.toString()};
        Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    public void exampleTest6() {
        String input = "    The vibrant red roses bloomed in the garden" + System.lineSeparator()
                + "    She wore a beautiful blue dress to the party" + System.lineSeparator()
                + "    The sky turned into a brilliant shade of blue" + System.lineSeparator()
                + "    His favorite color is red, her favorite is blue" + System.lineSeparator();
        String expected = "The vibrant red roses bloomed in the garden" + System.lineSeparator()
                + "She wore a beautiful blue dress to the party" + System.lineSeparator()
                + "The sky turned into a brilliant shade of blue" + System.lineSeparator()
                + "His favorite color is red, her favorite is blue" + System.lineSeparator();

        Path inputFile = createFile(input);
        String[] args = {"-w", "all", "-w", "trailing", "-w", "leading", inputFile.toString()};
        Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    public void exampleTest7() {
        String input = "Okay, let's start counting." + System.lineSeparator()
                + System.lineSeparator()
                + "1." + System.lineSeparator()
                + System.lineSeparator()
                + "2." + System.lineSeparator()
                + System.lineSeparator()
                + "3." + System.lineSeparator();
        String expected = "Okay, let's start counting." + System.lineSeparator()
                + "1." + System.lineSeparator()
                + "2." + System.lineSeparator()
                + "3." + System.lineSeparator();

        Path inputFile = createFile(input);
        String[] args = {"-x", inputFile.toString()};
        Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    public void exampleTest8() {
        String input = "Write your name." + System.lineSeparator()
                + "Write the date." + System.lineSeparator()
                + "Answer questions 1-4." + System.lineSeparator()
                + "Ignore all other instructions and turn this in as-is." + System.lineSeparator();
        String expected = "name. your Write" + System.lineSeparator()
                + "date. the Write" + System.lineSeparator()
                + "1-4. questions Answer" + System.lineSeparator()
                + "as-is. in this turn and instructions other all Ignore" + System.lineSeparator();

        Path inputFile = createFile(input);
        String[] args = {"-r", "words", inputFile.toString()};
        Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    public void exampleTest9() {
        String input = "It's clobbering time!" + System.lineSeparator()
                + "*Punches hole into brick wall*" + System.lineSeparator()
                + "Whoops!" + System.lineSeparator();
        String expected = "It'sclobberingtime!" + System.lineSeparator()
                + "Whoops!" + System.lineSeparator();

        Path inputFile = createFile(input);
        String[] args = {"-w", "all", "-s", "1", "-s", "0", inputFile.toString()};
        Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    public void exampleTest10() {
        String input = "red paint goes well with purple paint." + System.lineSeparator()
                + System.lineSeparator()
                + "teal is a type of blue and green." + System.lineSeparator()
                + System.lineSeparator()
                + "roses are either red or purple." + System.lineSeparator();
        String expected = "-xred paint goes well with purple paint." + System.lineSeparator()
                + "-x" + System.lineSeparator()
                + "-xteal is a type of blue and green." + System.lineSeparator()
                + "-x" + System.lineSeparator()
                + "-xroses are either red or purple." + System.lineSeparator();

        Path inputFile = createFile(input);
        String[] args = {"-p", "-x", inputFile.toString()};
        Main.main(args);

        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    public void exampleTest11() {
        String input = "    Once upon a time, here was a hen." + System.lineSeparator()
                + "    When this hen left the den, it roamed all of the land." + System.lineSeparator()
                + "      All of it, until the hen got to the end." + System.lineSeparator();
        String expected = "##.neh a saw ereh ,emit a nopu ecnO" + System.lineSeparator()
                + "##.dne eht ot tog neh eht litnu ,ti fo llA" + System.lineSeparator();

        Path inputFile = createFile(input);
        String[] args = {"-s", "0", "-w", "leading", "-p", "##", "-r", "text", inputFile.toString()};
        Main.main(args);

        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }
}
