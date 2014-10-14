#!/usr/bin/perl
my $student = $ARGV[0];
open CMD, "<$student/run.command";
my $command = <CMD>;
chomp $command;
close CMD;

open RUNSAMPLE, "<hw6_sample_solution/run.command";
my $sample_command = <RUNSAMPLE>;
close RUNSAMPLE;

open OUTFILE, ">$student/Test_Results";
open OUTFILEDISP, ">$student/Display_Problems";

my @files = `ls tests/*.valid tests/*.invalid`;

my $total_errors = 0;
foreach $file (@files) {
    my $errors = 0;
    chomp $file;
    open INFILE, "<$file";
    open DICTFILE, "<$file.dict";
    my $lineno = 0;
    while (my $line = <INFILE>) {
        my $dict = <DICTFILE>;
        chomp $dict;
        $lineno++;
        my $goal = $line;
        chomp $goal;
        #my $local_command = "timeout 10 $command";
        my $local_command = $command;
        $local_command =~ s/\$goal/$goal/;
        $local_command .= " > $student/temp";
        my $result_code = system($local_command);
        if ($result_code == 0) {
            if ($file =~ /\.invalid$/) {
                print OUTFILE "PROBLEM $file:$lineno: $dict\n";
                $errors++;
            }
            elsif ($file =~ /valid.valid$/) {
                open TEMPFILE, "<$student/temp";
                my @provided_output = <TEMPFILE>;
                close TEMPFILE;
                my $local_command_2 = $sample_command;
                $local_command_2 =~ s/\$goal/$goal/;
                my @correct_output = `$local_command_2`;
                my $problem = 0;
                if (@provided_output < 10) {
                    $problem = 1;
                }
                else {
                    for (my $i = 0; $i < 10; $i++) {
                        if ($provided_output[$i] ne $correct_output[$i]) {
                            $problem = 1;
                        }
                    }
                }
                if ($problem) {
                    print OUTFILEDISP "PROBLEM $file:$lineno: $dict\n";
                    print OUTFILEDISP "Should print:\n";
                    for (my $i = 0; $i < 10; $i++) {
                        print OUTFILEDISP $correct_output[$i];
                    }
                    print OUTFILEDISP "\nActually prints:\n";
                    for (my $i = 0; $i < 10 && $i < @provided_output; $i++) {
                        print OUTFILEDISP $provided_output[$i];
                    }
                    print OUTFILEDISP "\n";
                }
            }
        }
        #31744 is the return code from "timeout" when it times out
        elsif ($result_code == 31744) {
            print OUTFILE "TIMEOUT PROBLEM $file:$lineno: $dict\n";
            $errors++;
        }
        else {
            if ($file =~ /\.valid$/) {
                print OUTFILE "PROBLEM $file:$lineno: $dict\n";
                $errors++;
            }
        }
    }
    close INFILE;
    $total_errors += $errors;
    print OUTFILE "$file had $errors errors.\n";
}
print OUTFILE "Total errors: $total_errors\n";
close OUTFILE;
close OUTFILE2;
system("rm $student/temp");
