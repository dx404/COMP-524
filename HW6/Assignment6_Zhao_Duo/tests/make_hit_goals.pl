#!/usr/bin/perl
open BASE, "<hit_base";
open CMD, "<command";
my $command = <CMD>;
chomp $command;
close CMD;

open VOUTFILE, ">hit.valid";
open IOUTFILE, ">hit.invalid";

while (my $line = <BASE>) {
    my $set = $line;
    chomp $set;
    for (my $i = 1; $i <= 10; $i++) {
        for (my $j = 1; $j <= 10; $j++) {
            my $goal = "hit($i, $j, $set).";
            #print "Goal is: $goal\n";
            my $local_command = $command;
            $local_command =~ s/\$file/..\/hw6_sample_solution\/battleship.pl/;
            $local_command =~ s/\$goal/$goal/;
            if (system($local_command ) == 0) {
                print VOUTFILE "$goal\n";
            }
            else {
                print IOUTFILE "$goal\n";
            }
        }
    }
}

close IOUTFILE;
close VOUTFILE;
system("cp hit.valid hit.valid.dict");
system("cp hit.invalid hit.invalid.dict");
