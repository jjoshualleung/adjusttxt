package edu.gatech.seclass.adjusttxt;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.api.io.TempDir;

@Timeout(value = 1, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
public class MyMainTest {
    // Place all of your tests in this class, optionally using MainTest.java as an example
    private final String usageStr =
            "Usage: adjusttxt [ -s number | -w spacing | -x | -r target | -p prefix ] FILE"
                    + System.lineSeparator();

    @TempDir
    Path tempDirectory;

    @RegisterExtension
    OutputCapture capture = new OutputCapture();

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

    /**
     * Frame #: 1 - Test for empty file
     */
    @Test
    public void adjusttxtTest1() {
        Path inputFile = createFile("");
        String[] args = {inputFile.toString()};
        Main.main(args);

        Assertions.assertTrue(capture.stdout().isEmpty());
        Assertions.assertTrue(capture.stderr().isEmpty());
    }

    /*
     * Frame #: 2 - Missing newline at end
     */
    @Test
    public void adjusttxtTest2() {
        String input = "Missing newline at the end";
        Path inputFile = createFile(input);

        String[] args = {inputFile.toString()};
        Main.main(args);

        String expectedError = "Usage: adjusttxt [ -s number | -w spacing | -x | -r target | -p prefix ] FILE" + System.lineSeparator();
        Assertions.assertEquals(expectedError, capture.stderr());
        Assertions.assertTrue(capture.stdout().isEmpty());
    }

    /*
     * Frame #: 3 - Missing input file path
     */
    @Test
    public void adjusttxtTest3() {
        String[] args = {};
        Main.main(args);

        String expectedError = "Usage: adjusttxt [ -s number | -w spacing | -x | -r target | -p prefix ] FILE" + System.lineSeparator();
        Assertions.assertEquals(expectedError, capture.stderr());
        Assertions.assertTrue(capture.stdout().isEmpty());
    }

    /*
     * Frame #: 4 - Skip lines parameter out of range
     */
    @Test
    public void adjusttxtTest4() {
        String input = "Hello" + System.lineSeparator() + "Hello" + System.lineSeparator() + "Hello" + System.lineSeparator();
        Path inputFile = createFile(input);

        String[] args = {"-s", "100", inputFile.toString()};  // Out-of-range skip parameter
        Main.main(args);

        String expectedError = "Usage: adjusttxt [ -s number | -w spacing | -x | -r target | -p prefix ] FILE" + System.lineSeparator();
        Assertions.assertEquals(expectedError, capture.stderr());
        Assertions.assertTrue(capture.stdout().isEmpty());
    }

    /*
     * Frame #: 5 - Non-integer skip lines parameter
     */
    @Test
    public void adjusttxtTest5() {
        String input = "Hello" + System.lineSeparator() + "Hello" + System.lineSeparator() + "Hello" + System.lineSeparator();
        Path inputFile = createFile(input);

        String[] args = {"-s", "two", inputFile.toString()};
        Main.main(args);

        String expectedError = "Usage: adjusttxt [ -s number | -w spacing | -x | -r target | -p prefix ] FILE" + System.lineSeparator();
        Assertions.assertEquals(expectedError, capture.stderr());
        Assertions.assertTrue(capture.stdout().isEmpty());
    }

    /*
     * Frame #: 6 - Missing skip lines parameter
     */
    @Test
    public void adjusttxtTest6() {
        String input = "Hello" + System.lineSeparator() + "Hello" + System.lineSeparator();
        Path inputFile = createFile(input);

        String[] args = {"-s", inputFile.toString()};
        Main.main(args);

        String expectedError = "Usage: adjusttxt [ -s number | -w spacing | -x | -r target | -p prefix ] FILE" + System.lineSeparator();
        Assertions.assertEquals(expectedError, capture.stderr());
        Assertions.assertTrue(capture.stdout().isEmpty());
    }

    /*
     * Frame #: 7 - Unrecognized string for remove whitespace
     */
    @Test
    public void adjusttxtTest7() {
        String input = "Hello" + System.lineSeparator() + "Hello" + System.lineSeparator();
        Path inputFile = createFile(input);

        String[] args = {"-w", "UnrecognizedString", inputFile.toString()};
        Main.main(args);

        String expectedError = "Usage: adjusttxt [ -s number | -w spacing | -x | -r target | -p prefix ] FILE" + System.lineSeparator();
        Assertions.assertEquals(expectedError, capture.stderr());
        Assertions.assertTrue(capture.stdout().isEmpty());
    }

    /*
     * Frame #: 8 - Remove whitespace with missing parameter
     */
    @Test
    public void adjusttxtTest8() {
        String input = "How are you today";
        Path inputFile = createFile(input);

        String[] args = {"-w", inputFile.toString()};
        Main.main(args);

        String expectedError = "Usage: adjusttxt [ -s number | -w spacing | -x | -r target | -p prefix ] FILE" + System.lineSeparator();
        Assertions.assertEquals(expectedError, capture.stderr());
        Assertions.assertTrue(capture.stdout().isEmpty());
    }

    /*
     * Frame #: 9 - Remove empty lines with missing parameter
     */
    @Test
    public void adjusttxtTest9() {
        String input = "Hello" + System.lineSeparator() + System.lineSeparator() + "world" + System.lineSeparator();
        Path inputFile = createFile(input);

        String[] args = {"-x", "", inputFile.toString()};
        Main.main(args);

        String expectedError = "Usage: adjusttxt [ -s number | -w spacing | -x | -r target | -p prefix ] FILE" + System.lineSeparator();
        Assertions.assertEquals(expectedError, capture.stderr());
        Assertions.assertTrue(capture.stdout().isEmpty());
    }

    /*
     * Frame #: 10 - Reverse line with unrecognized string parameter
     */
    @Test
    public void adjusttxtTest10() {
        String input = "My name is Joshua";
        Path inputFile = createFile(input);

        String[] args = {"-r", "UnrecognizedOption", inputFile.toString()};
        Main.main(args);

        String expectedError = "Usage: adjusttxt [ -s number | -w spacing | -x | -r target | -p prefix ] FILE" + System.lineSeparator();
        Assertions.assertEquals(expectedError, capture.stderr());
        Assertions.assertTrue(capture.stdout().isEmpty());
    }

    /*
     * Frame #: 11 - Reverse line with missing parameter
     */
    @Test
    public void adjusttxtTest11() {
        String input = "My name is Joshua";
        Path inputFile = createFile(input);

        String[] args = {"-r", inputFile.toString()};
        Main.main(args);

        String expectedError = "Usage: adjusttxt [ -s number | -w spacing | -x | -r target | -p prefix ] FILE" + System.lineSeparator();
        Assertions.assertEquals(expectedError, capture.stderr());
        Assertions.assertTrue(capture.stdout().isEmpty());
    }

    /*
     * Frame #: 12 - Add prefix with empty string (no prefix)
     */
    @Test
    public void adjusttxtTest12() {
        String input = "Add prefix with empty string";
        Path inputFile = createFile(input);

        String[] args = {"-p", "", inputFile.toString()};
        Main.main(args);

        String expectedError = "Usage: adjusttxt [ -s number | -w spacing | -x | -r target | -p prefix ] FILE" + System.lineSeparator();
        Assertions.assertEquals(expectedError, capture.stderr());
        Assertions.assertTrue(capture.stdout().isEmpty());
    }

    /*
     * Frame #: 13 - Missing required option
     */
    @Test
    public void adjusttxtTest13() {
        String input = "I go to school by bus" + System.lineSeparator();
        Path inputFile = createFile(input);

        String[] args = {
                "-a", "0",  // Skip every even line
                inputFile.toString()
        };
        Main.main(args);

        String expectedError = "Usage: adjusttxt [ -s number | -w spacing | -x | -r target | -p prefix ] FILE" + System.lineSeparator();
        Assertions.assertTrue(capture.stderr().contains(expectedError));
        Assertions.assertTrue(capture.stdout().isEmpty());
    }

    /*
     * Frame #: 14 - Unrecognized option
     */
    @Test
    public void adjusttxtTest14() {
        String input = "I go to school by bus";
        Path inputFile = createFile(input);

        String[] args = {"-a", inputFile.toString()};  // Unrecognized option -a
        Main.main(args);

        String expectedError = "Usage: adjusttxt [ -s number | -w spacing | -x | -r target | -p prefix ] FILE" + System.lineSeparator();
        Assertions.assertEquals(expectedError, capture.stderr());
        Assertions.assertTrue(capture.stdout().isEmpty());
    }

    /*
     * Frame #: 15 - Non-empty file, skip every even line, remove leading whitespace, reverse words,
     * add prefix, with correct execution order and repeated options, remove whitespace present
     */
    @Test
    public void adjusttxtTest15() {
        String input = "Write your name." + System.lineSeparator() + "Write your name" + System.lineSeparator();
        String expectedOutput = "Prefixname. your Write" + System.lineSeparator();

        Path inputFile = createFile(input);
        String[] args = {
                "-s", "0",
                "-w", "leading",
                "-r", "words",
                "-p", "Prefix",
                "-s", "0",
                inputFile.toString()
        };
        Main.main(args);

        Assertions.assertEquals(expectedOutput, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    /*
     * Frame #: 16 - Non-empty file, skip every even line, remove leading whitespace, reverse words,
     * add prefix, with correct execution order and no repeated options, remove whitespace present
     */
    @Test
    public void adjusttxtTest16() {
        String input = "Write your name." + System.lineSeparator() + "Write your name" + System.lineSeparator();
        String expectedOutput = "Prefixname. your Write" + System.lineSeparator();

        Path inputFile = createFile(input);

        String[] args = {
                "-s", "0",
                "-w", "leading",
                "-r", "words",
                "-p", "Prefix",
                inputFile.toString()
        };
        Main.main(args);

        Assertions.assertEquals(expectedOutput, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    /*
     * Frame #: 17 - Non-empty file, skip every even line, remove leading whitespace, reverse words,
     * add prefix, with incorrect execution order and repeated options, remove whitespace present
     */
    @Test
    public void adjusttxtTest17() {
        String input = "Write your name." + System.lineSeparator() + "Write your name" + System.lineSeparator();
        String expectedOutput = "Prefixname. your Write" + System.lineSeparator();

        Path inputFile = createFile(input);

        String[] args = {
                "-r", "words",
                "-s", "0",
                "-p", "Prefix",
                "-s", "0",
                "-w", "leading",
                inputFile.toString()
        };
        Main.main(args);

        Assertions.assertEquals(expectedOutput, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    /*
     * Frame #: 18 - Non-empty file, skip every even line, remove leading whitespace, reverse words,
     * add prefix, with incorrect execution order and no repeated options, remove whitespace present
     */
    @Test
    public void adjusttxtTest18() {
        String input = "Write your name." + System.lineSeparator() + "Write your name" + System.lineSeparator();
        String expectedOutput = "Prefixname. your Write" + System.lineSeparator();

        Path inputFile = createFile(input);

        String[] args = {
                "-r", "words",  // incorrect order --> applied later
                "-s", "0",
                "-w", "leading",
                "-p", "Prefix",
                inputFile.toString()
        };
        Main.main(args);

        Assertions.assertEquals(expectedOutput, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    /*
     * Frame #: 19 - Non-empty file, skip every even line, remove leading whitespace, reverse whole line,
     * add prefix, with correct execution order and repeated options, remove whitespace present
     */
    @Test
    public void adjusttxtTest19() {
        String input = "Write your name." + System.lineSeparator() + "Write your name" + System.lineSeparator();
        String expectedOutput = "Prefix.eman ruoy etirW" + System.lineSeparator();

        Path inputFile = createFile(input);

        String[] args = {
                "-s", "0",
                "-w", "leading",
                "-r", "text",
                "-p", "Prefix",
                "-s", "0",
                inputFile.toString()
        };
        Main.main(args);

        Assertions.assertEquals(expectedOutput, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    /*
     * Frame #: 20 - Non-empty file, skip every even line, remove leading whitespace, reverse whole line,
     * add prefix, with correct execution order and no repeated options, remove whitespace present
     */
    @Test
    public void adjusttxtTest20() {
        String input = "Write your name." + System.lineSeparator() + "Write your name" + System.lineSeparator();
        String expectedOutput = "Prefix.eman ruoy etirW" + System.lineSeparator();

        Path inputFile = createFile(input);

        String[] args = {
                "-s", "0",
                "-w", "leading",
                "-r", "text",
                "-p", "Prefix",
                inputFile.toString()
        };
        Main.main(args);

        Assertions.assertEquals(expectedOutput, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    /*
     * Frame #: 21 - Non-empty file, skip every even line, remove leading whitespace, reverse whole line,
     * add prefix, with incorrect execution order and repeated options, remove whitespace present
     */
    @Test
    public void adjusttxtTest21() {
        String input = "Write your name." + System.lineSeparator() + "Write your name" + System.lineSeparator();
        String expectedOutput = "Prefix.eman ruoy etirW" + System.lineSeparator();

        Path inputFile = createFile(input);

        String[] args = {
                "-r", "text", // incorrect order --> applied later
                "-s", "0",
                "-p", "Prefix",
                "-s", "0",
                "-w", "leading",
                inputFile.toString()
        };
        Main.main(args);

        Assertions.assertEquals(expectedOutput, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    /*
     * Frame #: 22 - Non-empty file, skip every even line, remove leading whitespace, reverse whole line,
     * add prefix, with incorrect execution order and no repeated options, remove whitespace present
     */
    @Test
    public void adjusttxtTest22() {
        String input = "Write your name." + System.lineSeparator() + "Write your name" + System.lineSeparator();
        String expectedOutput = "Prefix.eman ruoy etirW" + System.lineSeparator();

        Path inputFile = createFile(input);

        String[] args = {
                "-r", "text",
                "-s", "0",
                "-w", "leading",
                "-p", "Prefix",
                inputFile.toString()
        };
        Main.main(args);

        Assertions.assertEquals(expectedOutput, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    /*
     * Frame #: 23 - Non-empty file, skip every even line, remove trailing whitespace, reverse words,
     * add prefix, with correct execution order and repeated options, remove whitespace present
     */
    @Test
    public void adjusttxtTest23() {
        String input = "Write your name.   " + System.lineSeparator() + "Write your name   " + System.lineSeparator();
        String expectedOutput = "Prefixname. your Write" + System.lineSeparator();

        Path inputFile = createFile(input);

        String[] args = {
                "-s", "0",
                "-w", "trailing",
                "-r", "words",
                "-p", "Prefix",
                "-s", "0",
                inputFile.toString()
        };
        Main.main(args);

        Assertions.assertEquals(expectedOutput, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    /*
     * Frame #: 24 - Non-empty file, skip every even line, remove trailing whitespace, reverse words,
     * add prefix, with correct execution order and no repeated options, remove whitespace present
     */
    @Test
    public void adjusttxtTest24() {
        String input = "Write your name.   " + System.lineSeparator() + "Write your name   " + System.lineSeparator();
        String expectedOutput = "Prefixname. your Write" + System.lineSeparator();

        Path inputFile = createFile(input);

        String[] args = {
                "-s", "0",
                "-w", "trailing",
                "-r", "words",
                "-p", "Prefix",
                inputFile.toString()
        };
        Main.main(args);

        Assertions.assertEquals(expectedOutput, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    /*
     * Frame #: 25 - Non-empty file, skip every even line, remove trailing whitespace, reverse words,
     * add prefix, with incorrect execution order and repeated options, remove whitespace present
     */
    @Test
    public void adjusttxtTest25() {
        String input = "Write your name.   " + System.lineSeparator() + "Write your name   " + System.lineSeparator();
        String expectedOutput = "Prefixname. your Write" + System.lineSeparator();

        Path inputFile = createFile(input);

        String[] args = {
                "-r", "words",  // incorrect order
                "-s", "0",
                "-p", "Prefix",
                "-s", "0",
                "-w", "trailing",
                inputFile.toString()
        };
        Main.main(args);

        Assertions.assertEquals(expectedOutput, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    /*
     * Frame #: 26 - Non-empty file, skip every even line, remove trailing whitespace, reverse words,
     * add prefix, with incorrect execution order and no repeated options, remove whitespace present
     */
    @Test
    public void adjusttxtTest26() {
        String input = "Write your name.   " + System.lineSeparator() + "Write your name   " + System.lineSeparator();
        String expectedOutput = "Prefixname. your Write" + System.lineSeparator();

        Path inputFile = createFile(input);

        // Set up arguments based on Test Case 25
        String[] args = {
                "-r", "words",  // incorrect order
                "-s", "0",
                "-w", "trailing",
                "-p", "Prefix",
                inputFile.toString()
        };
        Main.main(args);

        Assertions.assertEquals(expectedOutput, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    /*
     * Frame #: 27 - Non-empty file, skip every even line, remove trailing whitespace, reverse whole line,
     * add prefix, with correct execution order and repeated options, remove whitespace present
     */
    @Test
    public void adjusttxtTest27() {
        String input = "Write your name.   " + System.lineSeparator() + "Write your name   " + System.lineSeparator();
        String expectedOutput = "Prefix.eman ruoy etirW" + System.lineSeparator();

        Path inputFile = createFile(input);

        String[] args = {
                "-s", "0",
                "-w", "trailing",
                "-r", "text",
                "-p", "Prefix",
                "-s", "0",
                inputFile.toString()
        };
        Main.main(args);

        Assertions.assertEquals(expectedOutput, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    /*
     * Frame #: 28 - Non-empty file, skip every even line, remove trailing whitespace, reverse whole line,
     * add prefix, with correct execution order and no repeated options, remove whitespace present
     */
    @Test
    public void adjusttxtTest28() {
        String input = "Write your name.   " + System.lineSeparator() + "Write your name   " + System.lineSeparator();
        String expectedOutput = "Prefix.eman ruoy etirW" + System.lineSeparator();

        Path inputFile = createFile(input);

        String[] args = {
                "-s", "0",
                "-w", "trailing",
                "-r", "text",
                "-p", "Prefix",
                inputFile.toString()
        };
        Main.main(args);

        Assertions.assertEquals(expectedOutput, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    /*
     * Frame #: 29 - Non-empty file, skip every even line, remove trailing whitespace, reverse whole line,
     * add prefix, with incorrect execution order and repeated options, remove whitespace present
     */
    @Test
    public void adjusttxtTest29() {
        String input = "Write your name.   " + System.lineSeparator() + "Write your name   " + System.lineSeparator();
        String expectedOutput = "Prefix.eman ruoy etirW" + System.lineSeparator();

        Path inputFile = createFile(input);

        String[] args = {
                "-r", "text",
                "-s", "0",
                "-p", "Prefix",
                "-s", "0",
                "-w", "trailing",
                inputFile.toString()
        };
        Main.main(args);

        Assertions.assertEquals(expectedOutput, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    /*
     * Frame #: 30 - Non-empty file, skip every even line, remove trailing whitespace, reverse whole line,
     * add prefix, with incorrect execution order and no repeated options.
     */
    @Test
    public void adjusttxtTest30() {
        String input = "Write your name.   " + System.lineSeparator() + "Write your name   " + System.lineSeparator();
        String expectedOutput = "Prefix.eman ruoy etirW" + System.lineSeparator();

        Path inputFile = createFile(input);

        String[] args = {
                "-r", "text",  // incorrect order
                "-s", "0",
                "-w", "trailing",
                "-p", "Prefix",
                inputFile.toString()
        };
        Main.main(args);

        Assertions.assertEquals(expectedOutput, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    /*
     * Frame #: 31 -  Non-empty file, skip every even line, remove all whitespace, reverse words,
     * add prefix, with correct execution order and repeated options.
     */
    @Test
    public void adjusttxtTest31() {
        String input = "Write your name.   " + System.lineSeparator() + "Write your name   " + System.lineSeparator();
        String expectedOutput = "PrefixWriteyourname." + System.lineSeparator();

        Path inputFile = createFile(input);

        String[] args = {
                "-s", "0",
                "-w", "all",
                "-r", "words",
                "-p", "Prefix",
                "-s", "0",  // Repeated skip option
                inputFile.toString()
        };
        Main.main(args);

        Assertions.assertEquals(expectedOutput, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    /*
     * Frame #: 32 - Non-empty file, skip every even line, remove all whitespace, reverse words,
     * add prefix, with correct execution order and no repeated options.
     */
    @Test
    public void adjusttxtTest32() {
        String input = "Write your name.   " + System.lineSeparator() + "Write your name   " + System.lineSeparator();
        String expectedOutput = "PrefixWriteyourname." + System.lineSeparator();

        Path inputFile = createFile(input);

        String[] args = {
                "-s", "0",
                "-w", "all",
                "-r", "words",
                "-p", "Prefix",
                inputFile.toString()
        };
        Main.main(args);

        Assertions.assertEquals(expectedOutput, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    /*
     * Frame #: 33 - Non-empty file, skip every even line, remove all whitespace, reverse words,
     * add prefix, with incorrect execution order and repeated options.
     */
    @Test
    public void adjusttxtTest33() {
        String input = "Write your name.   " + System.lineSeparator() + "Write your name   " + System.lineSeparator();
        String expectedOutput = "PrefixWriteyourname." + System.lineSeparator();

        Path inputFile = createFile(input);

        String[] args = {
                "-r", "words",  // incorrect order, placed first
                "-s", "0",
                "-p", "Prefix",
                "-s", "0",
                "-w", "all",  // Remove all whitespace
                inputFile.toString()
        };
        Main.main(args);

        Assertions.assertEquals(expectedOutput, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    /*
     * Frame #: 34 - Non-empty file, skip every even line, remove all whitespace, reverse words,
     * add prefix, with incorrect execution order and no repeated options.
     */
    @Test
    public void adjusttxtTest34() {
        String input = "Write your name.   " + System.lineSeparator() + "Write your name   " + System.lineSeparator();
        String expectedOutput = "PrefixWriteyourname." + System.lineSeparator();

        Path inputFile = createFile(input);

        String[] args = {
                "-r", "words",  // Incorrect order
                "-s", "0",
                "-w", "all",
                "-p", "Prefix",
                inputFile.toString()
        };
        Main.main(args);

        Assertions.assertEquals(expectedOutput, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    /*
     * Frame #: 35 - Non-empty file, skip every even line, remove all whitespace, reverse whole line,
     * add prefix, with correct execution order and repeated options.
     */
    @Test
    public void adjusttxtTest35() {
        String input = "Write your name.   " + System.lineSeparator() + "Write your name   " + System.lineSeparator();
        String expectedOutput = "Prefix.emanruoyetirW" + System.lineSeparator();

        Path inputFile = createFile(input);

        String[] args = {
                "-s", "0",
                "-w", "all",
                "-r", "text",
                "-p", "Prefix",
                "-s", "0",
                inputFile.toString()
        };
        Main.main(args);

        Assertions.assertEquals(expectedOutput, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    /*
     * Frame #: 36 - Non-empty file, skip every even line, remove all whitespace, reverse whole line,
     * add prefix, with correct execution order and no repeated options.
     */
    @Test
    public void adjusttxtTest36() {
        String input = "Write your name.   " + System.lineSeparator() + "Write your name   " + System.lineSeparator();
        String expectedOutput = "Prefix.emanruoyetirW" + System.lineSeparator();

        Path inputFile = createFile(input);

        String[] args = {
                "-s", "0",
                "-w", "all",
                "-r", "text",
                "-p", "Prefix",
                inputFile.toString()
        };
        Main.main(args);

        Assertions.assertEquals(expectedOutput, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    /*
     * Frame #: 37 - Non-empty file, skip every even line, remove all whitespace, reverse whole line,
     * add prefix, with incorrect execution order and repeated options.
     */
    @Test
    public void adjusttxtTest37() {
        String input = "Write your name.   " + System.lineSeparator() + "Write your name   " + System.lineSeparator();
        String expectedOutput = "Prefix.emanruoyetirW" + System.lineSeparator();

        Path inputFile = createFile(input);

        String[] args = {
                "-r", "text", // incorrect order
                "-s", "0",
                "-w", "all",
                "-p", "Prefix",
                "-s", "0",
                inputFile.toString()
        };
        Main.main(args);

        Assertions.assertEquals(expectedOutput, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    /*
     * Frame #: 38 - Non-empty file, skip every even line, remove all whitespace, reverse whole line,
     * add prefix, with incorrect execution order and no repeated options.
     */
    @Test
    public void adjusttxtTest38() {
        String input = "Write your name.   " + System.lineSeparator() + "Write your name   " + System.lineSeparator();
        String expectedOutput = "Prefix.emanruoyetirW" + System.lineSeparator();

        Path inputFile = createFile(input);

        String[] args = {
                "-r", "text",
                "-s", "0",
                "-w", "all",
                "-p", "Prefix",
                inputFile.toString()
        };
        Main.main(args);

        Assertions.assertEquals(expectedOutput, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    /*
     * Frame #: 39 - Non-empty file, skip every odd line, remove leading whitespace, reverse words,
     * add prefix, with correct execution order and repeated options.
     */
    @Test
    public void adjusttxtTest39() {
        String input = "   Write your name.   " + System.lineSeparator() + "   Write your name" + System.lineSeparator();
        String expectedOutput = "Prefixname your Write" + System.lineSeparator();

        Path inputFile = createFile(input);

        String[] args = {
                "-s", "1",
                "-w", "leading",
                "-r", "words",
                "-p", "Prefix",
                "-s", "1",
                inputFile.toString()
        };
        Main.main(args);

        Assertions.assertEquals(expectedOutput, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    /*
     * Frame #: 40 - Non-empty file, skip every odd line, remove leading whitespace, reverse words,
     * add prefix, with correct execution order and no repeated options.
     */
    @Test
    public void adjusttxtTest40() {
        String input = "   Write your name." + System.lineSeparator() + "   Write your name" + System.lineSeparator();
        String expectedOutput = "Prefixname your Write" + System.lineSeparator();

        Path inputFile = createFile(input);

        String[] args = {
                "-s", "1",
                "-w", "leading",
                "-r", "words",
                "-p", "Prefix",
                inputFile.toString()
        };
        Main.main(args);

        Assertions.assertEquals(expectedOutput, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    /*
     * Frame #: 41 - Non-empty file, skip every odd line, remove leading whitespace, reverse words,
     * add prefix, with incorrect execution order and repeated options, remove whitespace present
     */
    @Test
    public void adjusttxtTest41() {
        String input = "   Write your name." + System.lineSeparator() + "   Write your name" + System.lineSeparator();
        String expectedOutput = "Prefixname your Write" + System.lineSeparator();

        Path inputFile = createFile(input);

        String[] args = {
                "-r", "words",
                "-s", "1",
                "-p", "Prefix",
                "-s", "1",
                "-w", "leading",
                inputFile.toString()
        };
        Main.main(args);

        Assertions.assertEquals(expectedOutput, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    /*
     * Frame #: 42 - Non-empty file, skip every odd line, remove leading whitespace, reverse words,
     * add prefix, with incorrect execution order and no repeated options, remove whitespace present
     */
    @Test
    public void adjusttxtTest42() {
        String input = "   Write your name." + System.lineSeparator() + "   Write your name" + System.lineSeparator();
        String expectedOutput = "Prefixname your Write" + System.lineSeparator();

        Path inputFile = createFile(input);

        String[] args = {
                "-r", "words",
                "-s", "1",
                "-w", "leading",
                "-p", "Prefix",
                inputFile.toString()
        };
        Main.main(args);

        Assertions.assertEquals(expectedOutput, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    /*
     * Frame #: 43 - Non-empty file, skip every odd line, remove leading whitespace, reverse whole line,
     * add prefix, with correct execution order and repeated options, remove whitespace present
     */
    @Test
    public void adjusttxtTest43() {
        String input = "   Write your name." + System.lineSeparator() + "   Write your name" + System.lineSeparator();
        String expectedOutput = "Prefixeman ruoy etirW" + System.lineSeparator();

        Path inputFile = createFile(input);

        String[] args = {
                "-s", "1",
                "-w", "leading",
                "-r", "text",
                "-p", "Prefix",
                "-s", "1",
                inputFile.toString()
        };
        Main.main(args);

        Assertions.assertEquals(expectedOutput, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    /*
     * Frame #: 44 - Non-empty file, skip every odd line, remove leading whitespace, reverse whole line,
     * add prefix, with correct execution order and no repeated options, remove whitespace present
     */
    @Test
    public void adjusttxtTest44() {
        String input = "   Write your name." + System.lineSeparator() + "   Write your name" + System.lineSeparator();
        String expectedOutput = "Prefixeman ruoy etirW" + System.lineSeparator();

        Path inputFile = createFile(input);

        String[] args = {
                "-s", "1",
                "-w", "leading",
                "-r", "text",
                "-p", "Prefix",
                inputFile.toString()
        };
        Main.main(args);

        Assertions.assertEquals(expectedOutput, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    /*
     * Frame #: 45 - Non-empty file, skip every odd line, remove leading whitespace, reverse whole line,
     * add prefix, with incorrect execution order and repeated options, remove whitespace present
     */
    @Test
    public void adjusttxtTest45() {
        String input = "   Write your name." + System.lineSeparator() + "   Write your name" + System.lineSeparator();
        String expectedOutput = "Prefixeman ruoy etirW" + System.lineSeparator();

        Path inputFile = createFile(input);

        String[] args = {
                "-r", "text",
                "-s", "1",
                "-p", "Prefix",
                "-s", "1",
                "-w", "leading",
                inputFile.toString()
        };
        Main.main(args);

        Assertions.assertEquals(expectedOutput, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    /*
     * Frame #: 46 - Non-empty file, skip every odd line, remove leading whitespace, reverse whole line,
     * add prefix, with incorrect execution order and no repeated options, remove whitespace present
     */
    @Test
    public void adjusttxtTest46() {
        String input = "   My name is Joshua" + System.lineSeparator() +
                "   Leung is my surname" + System.lineSeparator() +
                "   Hello world" + System.lineSeparator();
        String expectedOutput = "Mr.emanrus ym si gnueL" + System.lineSeparator();

        Path inputFile = createFile(input);

        String[] args = {
                "-r", "text",
                "-s", "1",
                "-w", "leading",
                "-p", "Mr.",
                inputFile.toString()
        };
        Main.main(args);

        Assertions.assertEquals(expectedOutput, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    /*
     * Frame #: 47 - Non-empty file, skip every odd line, remove trailing whitespace, reverse words,
     * add prefix, correct execution order and repeated options, remove whitespace present
     */
    @Test
    public void adjusttxtTest47() {
        String input = "My name is Joshua   " + System.lineSeparator() +
                "Leung is my surname   " + System.lineSeparator() +
                "Hello world   " + System.lineSeparator();
        String expectedOutput = "Mr.surname my is Leung" + System.lineSeparator();

        Path inputFile = createFile(input);

        String[] args = {
                "-s", "1",
                "-w", "trailing",
                "-r", "words",
                "-p", "Mr.",
                "-s", "1",
                inputFile.toString()
        };
        Main.main(args);

        Assertions.assertEquals(expectedOutput, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    /*
     * Frame #: 48 - Non-empty file, skip every odd line, remove trailing whitespace, reverse words,
     * add prefix with correct execution order and no repeated options, remove whitespace present
     */
    @Test
    public void adjusttxtTest48() {
        String input = "My name is Joshua   " + System.lineSeparator() +
                "Leung is my surname   " + System.lineSeparator() +
                "Hello world   " + System.lineSeparator();
        String expectedOutput = "Mr.surname my is Leung" + System.lineSeparator();

        Path inputFile = createFile(input);

        String[] args = {
                "-s", "1",
                "-w", "trailing",
                "-r", "words",
                "-p", "Mr.",
                inputFile.toString()
        };
        Main.main(args);

        Assertions.assertEquals(expectedOutput, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    /*
     * Frame #: 49 - Non-empty file, skip every odd line, remove trailing whitespace, reverse words,
     * add prefix with incorrect execution order and repeated options, remove whitespace present
     */
    @Test
    public void adjusttxtTest49() {
        String input = "My name is Joshua   " + System.lineSeparator() +
                "Leung is my surname   " + System.lineSeparator() +
                "Hello world   " + System.lineSeparator();
        String expectedOutput = "Mr.surname my is Leung" + System.lineSeparator();

        Path inputFile = createFile(input);

        String[] args = {
                "-r", "words",
                "-s", "1",
                "-p", "Mr.",
                "-s", "1",
                "-w", "trailing",
                inputFile.toString()
        };
        Main.main(args);

        Assertions.assertEquals(expectedOutput, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    /*
     * Frame #: 50 - Non-empty file, skip every odd line, remove trailing whitespace, reverse words,
     * add prefix with incorrect execution order and no repeated options, remove whitespace present
     */
    @Test
    public void adjusttxtTest50() {
        String input = "My name is Joshua   " + System.lineSeparator() +
                "Leung is my surname   " + System.lineSeparator() +
                "Hello world   " + System.lineSeparator();
        String expectedOutput = "Mr.surname my is Leung" + System.lineSeparator();

        Path inputFile = createFile(input);

        String[] args = {
                "-r", "words",
                "-s", "1",
                "-w", "trailing",
                "-p", "Mr.",
                inputFile.toString()
        };
        Main.main(args);

        Assertions.assertEquals(expectedOutput, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    /*
     * Frame #: 51 - Non-empty file, skip every odd line, remove trailing whitespace, reverse whole line,
     * add prefix "Mr.", with correct execution order and repeated options, remove whitespace present
     */
    @Test
    public void adjusttxtTest51() {
        String input = "My name is Joshua   " + System.lineSeparator() +
                "Leung is my surname   " + System.lineSeparator() +
                "Hello world   " + System.lineSeparator();
        String expectedOutput = "Mr.emanrus ym si gnueL" + System.lineSeparator();

        Path inputFile = createFile(input);

        String[] args = {
                "-s", "1",
                "-w", "trailing",
                "-r", "text",
                "-p", "Mr.",
                "-s", "1",
                inputFile.toString()
        };
        Main.main(args);

        Assertions.assertEquals(expectedOutput, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    /*
     * Frame #: 52 - Non-empty file, skip every odd line, remove trailing whitespace, reverse whole line,
     * add prefix with correct execution order and no repeated options, remove whitespace present
     */
    @Test
    public void adjusttxtTest52() {
        String input = "My name is Joshua   " + System.lineSeparator() +
                "Leung is my surname   " + System.lineSeparator() +
                "Hello world   " + System.lineSeparator();
        String expectedOutput = "Mr.emanrus ym si gnueL" + System.lineSeparator();

        Path inputFile = createFile(input);

        String[] args = {
                "-s", "1",
                "-w", "trailing",
                "-r", "text",
                "-p", "Mr.",
                inputFile.toString()
        };
        Main.main(args);

        Assertions.assertEquals(expectedOutput, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    /*
     * Frame #: 53 - Incorrect execution order and repeated options
     */
    @Test
    public void adjusttxtTest53() {
        String input = "My name is Joshua   " + System.lineSeparator() +
                "Leung is my surname   " + System.lineSeparator() +
                "Hello world   " + System.lineSeparator();
        String expectedOutput = "Mr.emanrus ym si gnueL" + System.lineSeparator();

        Path inputFile = createFile(input);

        String[] args = {
                "-r", "text",  // Incorrect order
                "-s", "1",
                "-p", "Mr.",
                "-s", "1",
                "-w", "trailing",
                inputFile.toString()
        };
        Main.main(args);

        Assertions.assertEquals(expectedOutput, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    /*
     * Frame #: 54 - Incorrect execution order and no repeated options
     */
    @Test
    public void adjusttxtTest54() {
        String input = "My name is Joshua   " + System.lineSeparator() +
                "Leung is my surname   " + System.lineSeparator() +
                "Hello world   " + System.lineSeparator();
        String expectedOutput = "Mr.emanrus ym si gnueL" + System.lineSeparator();

        Path inputFile = createFile(input);

        String[] args = {
                "-r", "text",  // Incorrect order
                "-s", "1",
                "-w", "trailing",
                "-p", "Mr.",
                inputFile.toString()
        };
        Main.main(args);

        Assertions.assertEquals(expectedOutput, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    /*
     * Frame #: 55 - Non-empty file, skip every odd line, remove all whitespace, reverse words,
     * add prefix with correct execution order and repeated options
     */
    @Test
    public void adjusttxtTest55() {
        String input = "My name is Joshua   " + System.lineSeparator() +
                "Leung is my surname   " + System.lineSeparator() +
                "Hello world   " + System.lineSeparator();
        String expectedOutput = "Mr.Leungismysurname" + System.lineSeparator();

        Path inputFile = createFile(input);

        String[] args = {
                "-s", "1",
                "-w", "all",
                "-r", "words",
                "-p", "Mr.",
                "-s", "1",
                inputFile.toString()
        };
        Main.main(args);

        Assertions.assertEquals(expectedOutput, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    /*
     * Frame #: 56 - Non-empty file, skip every odd line, remove all whitespace, reverse words,
     * add prefix with correct execution order and no repeated options
     */
    @Test
    public void adjusttxtTest56() {
        String input = "My name is Joshua   " + System.lineSeparator() +
                "Leung is my surname   " + System.lineSeparator() +
                "Hello world   " + System.lineSeparator();
        String expectedOutput = "Mr.Leungismysurname" + System.lineSeparator();

        Path inputFile = createFile(input);

        String[] args = {
                "-s", "1",
                "-w", "all",
                "-r", "words",
                "-p", "Mr.",
                inputFile.toString()
        };
        Main.main(args);

        Assertions.assertEquals(expectedOutput, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    /*
     * Frame #: 57 - Non-empty file, skip every odd line, remove all whitespace, reverse words,
     * add prefix but with incorrect execution order and repeated options
     */
    @Test
    public void adjusttxtTest57() {
        String input = "My name is Joshua   " + System.lineSeparator() +
                "Leung is my surname   " + System.lineSeparator() +
                "Hello world   " + System.lineSeparator();
        String expectedOutput = "Mr.Leungismysurname" + System.lineSeparator();

        Path inputFile = createFile(input);

        String[] args = {
                "-r", "words",  // Incorrect order
                "-s", "1",
                "-p", "Mr.",
                "-s", "1",
                "-w", "all",
                inputFile.toString()
        };
        Main.main(args);

        Assertions.assertEquals(expectedOutput, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    /*
     * Frame #: 58 - Non-empty file, skip every odd line, remove all whitespace, reverse words,
     * add prefix with incorrect execution order and no repeated options
     */
    @Test
    public void adjusttxtTest58() {
        String input = "My name is Joshua   " + System.lineSeparator() +
                "Leung is my surname   " + System.lineSeparator() +
                "Hello world   " + System.lineSeparator();
        String expectedOutput = "Mr.Leungismysurname" + System.lineSeparator();

        Path inputFile = createFile(input);

        String[] args = {
                "-r", "words",  // Incorrect order
                "-s", "1",
                "-w", "all",
                "-p", "Mr.",
                inputFile.toString()
        };
        Main.main(args);

        Assertions.assertEquals(expectedOutput, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    /*
     * Frame #: 59 - Non-empty file, skip every odd line, remove all whitespace, reverse whole line,
     * add prefix with correct execution order and repeated options
     */
    @Test
    public void adjusttxtTest59() {
        String input = "My name is Joshua   " + System.lineSeparator() +
                "Leung is my surname   " + System.lineSeparator() +
                "Hello world   " + System.lineSeparator();
        String expectedOutput = "Mr.emanrusymsignueL" + System.lineSeparator();

        Path inputFile = createFile(input);

        String[] args = {
                "-s", "1",
                "-w", "all",
                "-r", "text",
                "-p", "Mr.",
                "-s", "1",
                inputFile.toString()
        };
        Main.main(args);

        Assertions.assertEquals(expectedOutput, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    /*
     * Frame #: 60 - Non-empty file, skip every odd line, remove all whitespace, reverse whole line,
     * add prefix with correct execution order but with no repeated options
     */
    @Test
    public void adjusttxtTest60() {
        String input = "My name is Joshua   " + System.lineSeparator() +
                "Leung is my surname   " + System.lineSeparator() +
                "Hello world   " + System.lineSeparator();
        String expectedOutput = "Mr.emanrusymsignueL" + System.lineSeparator();

        Path inputFile = createFile(input);

        String[] args = {
                "-s", "1",
                "-w", "all",
                "-r", "text",
                "-p", "Mr.",
                inputFile.toString()
        };
        Main.main(args);

        Assertions.assertEquals(expectedOutput, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    /*
     * Frame #: 61 - Incorrect execution order and repeated options
     */
    @Test
    public void adjusttxtTest61() {
        String input = "My name is Joshua   " + System.lineSeparator() +
                "Leung is my surname   " + System.lineSeparator() +
                "Hello world   " + System.lineSeparator();
        String expectedOutput = "Mr.emanrusymsignueL" + System.lineSeparator();

        Path inputFile = createFile(input);

        String[] args = {
                "-r", "text",  // Incorrect order
                "-s", "1",
                "-p", "Mr.",
                "-s", "1",
                "-w", "all",
                inputFile.toString()
        };
        Main.main(args);

        Assertions.assertEquals(expectedOutput, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    /*
     * Frame #: 62 - Non-empty file, skip every odd line, remove all whitespace, reverse whole line,
     * add prefix  but with incorrect execution order and no repeated options
     */
    @Test
    public void adjusttxtTest62() {
        String input = "My name is Joshua   " + System.lineSeparator() +
                "Leung is my surname   " + System.lineSeparator() +
                "Hello world   " + System.lineSeparator();
        String expectedOutput = "Mr.emanrusymsignueL" + System.lineSeparator();

        Path inputFile = createFile(input);

        String[] args = {
                "-r", "text",  // Incorrect order
                "-s", "1",
                "-w", "all",
                "-p", "Mr.",
                inputFile.toString()
        };
        Main.main(args);

        Assertions.assertEquals(expectedOutput, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    /* ------------------------------- Additional Test Cases ------------------------------- */

    /*
     * Frame #: 63 - Test -x Remove empty line
     * Non-empty file, reverse words,
     * add prefix, correct execution order and no repeated options
     */
    @Test
    public void adjusttxtTest63() {
        String input = "My name is Joshua" + System.lineSeparator() // 1
                + System.lineSeparator() // 2
                + "Hello world" + System.lineSeparator() // 3
                + System.lineSeparator(); // 4

        String expectedOutput = "Mr.Joshua is name My" + System.lineSeparator() +
                "Mr.world Hello" + System.lineSeparator();

        Path inputFile = createFile(input);

        String[] args = {
                "-x",
                "-r", "words",
                "-p", "Mr.",
                inputFile.toString()
        };
        Main.main(args);

        Assertions.assertEquals(expectedOutput, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }
}