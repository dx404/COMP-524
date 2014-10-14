#!/usr/bin/perl
#initial setup
system("rm -rf tests/automated/*");
system("mkdir tests/automated/tests");
system("mkdir tests/automated/tests/good");
system("mkdir tests/automated/tests/bad");

my @good_files = `find tests/good -name '*.calc'`;
my @bad_files = `find tests/bad -name '*.calc'`;

sub genresults {
    my $basename = shift;
    system("java -cp sample_solution Main tests/automated/${basename}.calc > tests/automated/${basename}.results");
    system("cp tests/automated/${basename}.results tests/automated/${basename}.eof.results");
    system("echo 'eof(\$\$)' >> tests/automated/${basename}.eof.results");
}

foreach $file (@good_files) {
    chomp $file;
    $file =~ /^(.*)\.calc$/;
    my $basename = $1;
    system("cp $file tests/automated/tests/good/");
    genresults($basename);
    open INFILE, "<$file";
    my @lines = <INFILE>;
    close INFILE;
    open OUTFILE2, ">tests/automated/${basename}Tabs.calc";
    for (my $i = 0; $i < @lines; $i++) {
        $line = $lines[$i];
        $line_tabs = $lines[$i];
        $line_tabs =~ s/\s/\t/g;
        print OUTFILE2 $line_tabs;
        open OUTFILE, ">tests/automated/${basename}-$i.calc";
        print OUTFILE $line;
        close OUTFILE;
        genresults("$basename-$i");
        open OUTFILE, ">tests/automated/${basename}-${i}Chomped.calc";
        chomp $line;
        print OUTFILE $line;
        close OUTFILE;
        genresults("$basename-${i}Chomped");
    }
    close OUTFILE2;
    genresults("${basename}Tabs");
#Create sub-tests
    if (@lines > 0) {
        my $lastline = @lines - 1;
        chomp $lines[$lastline];
        open OUTFILE, ">tests/automated/${basename}Chomped.calc";
        foreach $line (@lines) {
            print OUTFILE $line;
        }
        close OUTFILE;
        genresults("${basename}Chomped");
    }
}

system("rm tests/automated/tests/good/DivAndMultiLineComment-*");
system("rm tests/automated/tests/good/MultiLineComment2-*");
system("rm tests/automated/tests/good/DivAndSingleLineComment-*");
system("rm tests/automated/tests/good/SingleLineComment2-*");
system("rm tests/automated/tests/good/DivAndSingleLineCommentTabs*");
system("rm tests/automated/tests/good/SingleLineComment2Tabs*");

foreach $file (@bad_files) {
    chomp $file;
    $file =~ /^(.*).calc$/;
    my $basename = $1;

    system("cp $file tests/automated/tests/bad/");
    open INFILE, "<$file";
    my @lines = <INFILE>;
    for (my $i = 0; $i < @lines; $i++) {
        $line = $lines[$i];
        open OUTFILE, ">tests/automated/${basename}-$i.calc";
        print OUTFILE $line;
        close OUTFILE;
        open OUTFILE, ">tests/automated/$basename-${i}Chomped.calc";
        chomp $line;
        print OUTFILE $line;
        close OUTFILE;
    }
}
