Your graded work is in students/{Your Name}
Comments have been added to code with // GRADING: - search for this word to
find them.

FinalGradingNotes.txt has your final grade.  Other files are output from tests.

From the Assignment2_{Name} directory (that this file is in), you can run:

./prep_tests.pl  - creates the automated tests I used.
./evaluate_good.pl students/{Name}    - to generate PositiveTest_Results.
                                        Valid tokens that should be recognized
./evaluate_bad.pl students/{Name}     - to generate NegativeTest_Results
                                        Invalid tokens that should produce
                                        errors (unless you parsed them as
                                        valid tokens with no whitespace)
./test_badfile.pl students/{Name}     - To test your program with a nonexistant
                                        file


Inside students/{Name} you also have run.command - each test is substituted
for $file when running a test.  Change this if you fix how filenames are
accepted.  The command will be run from Assignment2_{Name}
