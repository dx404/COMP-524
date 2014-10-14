#!/usr/bin/perl

my $file = $ARGV[0];
open INFILE, "<$file";
open OUTFILE, ">$file.new";
while (my $line = <INFILE>) {
    if ($line !~ /print OUTFILE2/) {
        print OUTFILE $line;
    }
    if ($line =~ /print OUTFILE set_string\(ship_list\((\$\w), (\$\w),/) {
        my $first = $1;
        my $second = $2;
        $line = <INFILE>;
        print OUTFILE $line;
        $line =~ /\s*(\S+)\(/;
        my $func = $1;
        $line = <INFILE>;
        print OUTFILE $line;
        print OUTFILE "        print OUTFILE2 \"$func($first, $second)\\n\";\n";
    }
}
close INFILE;
close OUTFILE;
system("mv $file.new $file");
