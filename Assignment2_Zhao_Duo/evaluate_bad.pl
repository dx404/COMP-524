#!/usr/bin/perl
my $verbose = 0;
if ($ARGV[1] eq '-v') {
    $verbose = 1;
}

if (@ARGV == 0) {
    die "Must provide directory!\n";
}
my $dir = $ARGV[0];

open INFILE, "<$dir/run.command";
my $command = <INFILE>;
close INFILE;
chomp $command;

open OUTFILE, ">$dir/NegativeTest_Results";

my @tests = `find tests/automated/tests/bad/ -name '*.calc'`;
my $num_suspicious = 0;
my $num_total = 0;
foreach $test (@tests) {
    $num_total++;
    chomp $test;
    my $localcommand = $command;
    $localcommand =~ s/\$file/$test/g;
    my @output = `$localcommand 2>&1`;
# Suspicious until error found.
    my $suspicious = 1;
    foreach $line (@output) {
#        if ($line =~ /^(\w+)\(.*\)$/) {
#            # Don't care about seeing eof line when no other problem
#            if ($1 ne "eof") {
#                $suspicious = 1;
#            }
#        }
        # If we're seeing an error message, probably no bad token.
        if ($line =~ /error/i || $line =~ /invalid/i || $line =~ /bad/i
               || $line =~ /exception/i || $line =~ /ERR/
               || $line =~ /malformed/i) {
            $suspicious = 0;
        }
    }
    if ($suspicious) {
        $num_suspicious++;
    }
    if ($suspicious || $verbose) {
        print OUTFILE "Test $test suspicious.\n";
        foreach $line (@output) {
            print OUTFILE $line;
        }
    }
}

print OUTFILE "$num_suspicious suspicious out of $num_total\n";

close OUTFILE;
