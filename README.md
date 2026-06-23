# adjusttxt

A Java command-line utility for simple text manipulation. `adjusttxt` reads a text
file, applies a sequence of transformations selected through command-line options,
and writes the result to standard output **without modifying the input file**.

This project was developed using an agile, test-driven development process: the test
suite was designed first (using the Category-Partition method) and the implementation
was built to satisfy it. The code is provided both as a command-line tool (`Main`) and
as a reusable library that implements a defined interface (`AdjustTxt`).

## Features

The program applies transformations based on the options provided. Options can appear
in any order; the input file must always be the **last** argument.

| Option | Parameter | Description |
|--------|-----------|-------------|
| `-s` | `0` or `1` | **Skip lines.** `0` skips every even line, `1` skips every odd line (the first line is line 1). |
| `-w` | `leading`, `trailing`, or `all` | **Remove whitespace** at the start of, end of, or everywhere in each line. Mutually exclusive with `-x`. |
| `-x` | *(none)* | **Remove empty lines.** Mutually exclusive with `-w`. |
| `-r` | `words` or `text` | **Reverse a line.** `words` reverses the order of words; `text` reverses the whole line character by character. |
| `-p` | `<prefix>` | **Add a prefix** to the start of every line. |

### Order of execution

Options are parsed left to right but always **executed in a fixed order**, so the
result does not depend on the order the options were typed:

```
-s  →  -w / -x  →  -r  →  -p
```

If an option is repeated, only its **last** occurrence is applied.

## Usage

```
adjusttxt [ -s number | -w spacing | -x | -r target | -p prefix ] FILE
```

### Examples

Reverse the order of words on every line:

```
$ adjusttxt -r words notes.txt
# "Write your name."  ->  "name. your Write"
```

Chain several transformations (skip even lines, strip leading whitespace, reverse the
text, then add a `##` prefix):

```
$ adjusttxt -s 0 -w leading -p ## -r text input.txt
```

## Building and running

This project targets **JDK 21**. From the `adjusttxt/` directory:

```bash
# Compile sources and tests
mkdir -p classes
javac -cp "lib/*" -d classes \
    src/edu/gatech/seclass/adjusttxt/*.java \
    test/edu/gatech/seclass/adjusttxt/*.java

# Run the command-line tool
java -cp classes edu.gatech.seclass.adjusttxt.Main -r words input.txt

# Run the JUnit 5 test suite
java -cp "classes:lib/*" org.junit.platform.console.ConsoleLauncher \
    --select-class edu.gatech.seclass.adjusttxt.MyMainTest
```

> On Windows, use `;` instead of `:` in the classpath, e.g. `-cp "classes;lib/*"`.

## Project structure

```
adjusttxt/
├── src/edu/gatech/seclass/adjusttxt/
│   ├── Main.java                 # Command-line entry point
│   ├── AdjustTxt.java            # Library implementation of the interface
│   ├── AdjustTxtInterface.java   # Public API contract
│   └── AdjustTxtException.java   # Thrown on invalid input (library mode)
├── test/edu/gatech/seclass/adjusttxt/
│   ├── MyMainTest.java           # Test suite for the CLI (~75 cases)
│   ├── MyLibTest.java            # Test suite for the library API
│   ├── MainTest.java             # Example tests from the specification
│   └── OutputCapture.java        # JUnit 5 extension to capture stdout/stderr
└── lib/                          # JUnit 5 console launcher
catpart.txt                       # Category-Partition test specification
catpart.txt.tsl                   # Test frames generated from catpart.txt
```

## CLI vs. library behavior

The same core logic is exposed two ways:

- **Command-line (`Main`):** invalid input prints the standard usage message to
  standard error.
- **Library (`AdjustTxt`):** because it is called programmatically, invalid input
  throws an `AdjustTxtException` instead of printing to the console.

## Error handling

All error conditions are handled, including:

- A missing input file, or no file path provided.
- A non-empty file whose last line is not terminated by a newline.
- Missing option parameters or unrecognized options.
- Invalid parameter values (e.g. `-s 2`, `-w middle`).
- Using `-x` together with `-w`.

An empty input file produces empty output.
