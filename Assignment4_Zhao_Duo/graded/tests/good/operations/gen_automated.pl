#!/usr/bin/perl

my @operators = ("+", "-", "*", "/", "^");
my @operator_names = ("Plus", "Minus", "Times", "Divide", "Exponent");
my @files = `ls *.precalc`;
foreach $file (@files) {
    chomp $file;
    $file =~ /^(.*)\.precalc/;
    my $basename = $1;
    open INFILE, "<$file";
    my @lines = <INFILE>;
    close INFILE;
    for (my $i = 0; $i < @operators; $i++) {
        my @lines_local = @lines;
        my $operator = $operators[$i];
        my $operator_name = $operator_names[$i];
        open OUTFILE, ">automated/${basename}${operator_name}.calc";
        foreach $line (@lines_local) {
            $line =~ s/\$operation/$operator/g;
            print OUTFILE $line;
        }
        close OUTFILE;
    }
}
