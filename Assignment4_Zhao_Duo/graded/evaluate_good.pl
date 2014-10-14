#!/usr/bin/perl
my @tests = `find tests/good/ -name '*.calc'`;

if (@ARGV == 0) {
    die "Must provide directory!\n";
}
$dir = $ARGV[0];
open INFILE, "<$dir/run.command";
my $command = <INFILE>;
chomp $command;
close INFILE;

open OUTFILE, ">$dir/PositiveTest_Results";
my $failed_tests = 0;
my $total_tests = 0;
foreach $test (@tests) {
    $total_tests++;
    chomp $test;
    $test =~ /^(.*)\.calc$/;
    my $basename = $1;
    my $localcommand = $command;
    $localcommand =~ s/\$file/$test/g;
    $localcommand .= " 2>&1";
    my $output = `$localcommand`;
    if ($output !~ /^Valid calculator source file$/) {
        $failed_tests++;
        print OUTFILE "Failed test ${basename}!\n";
        print OUTFILE $output;
    }
}

print OUTFILE "Failed $failed_tests out of $total_tests tests.\n";
close OUTFILE;
