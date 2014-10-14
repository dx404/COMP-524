Your graded work is in students/{Your Name}
Comments have been added to code with // GRADING: - search for this word to
find them.

FinalGradingNotes.txt has your final grade.  Other files are output from tests.

From the Assignment2_{Name} directory (that this file is in), you can run:

./evaluate_good.pl students/{Name}    - to generate PositiveTest_Results.
                                        Valid programs that should be recognized
./evaluate_bad.pl students/{Name}     - to generate NegativeTest_Results
                                        Invalid programs that should produce
                                        errors.

You can run

chmod 755 evaluate_good.pl evaluate_bad.pl

first if you are getting permission errors.

Inside students/{Name} you also have run.command - each test is substituted
for $file when running a test.  Change this if you fix how filenames are
accepted.  The command will be run from Assignment4_{Name}
