#!/usr/bin/perl
my @tests = `find tests/automated/tests/good/ -name '*.calc'`;

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
    $localcommand .= " > $dir/output 2>&1";
    system($localcommand);
    my $differ1 = `diff -q $dir/output ${basename}.results`;
    my $differ2 = `diff -q $dir/output ${basename}.eof.results`;
    if (($differ1 =~ /differ/)
            && ($differ2 =~ /differ/)) {
        $failed_tests++;
        print OUTFILE "Failed test ${basename}!\n";
        my @lines = `diff -u ${basename}.results $dir/output`;
        foreach $line (@lines) {
            print OUTFILE $line;
        }
    }
}

print OUTFILE "Failed $failed_tests out of $total_tests tests.\n";
close OUTFILE;
system("rm $dir/output");
