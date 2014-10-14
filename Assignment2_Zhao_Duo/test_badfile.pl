#!/usr/bin/perl
my $dir = $ARGV[0];

open INFILE, "<$dir/run.command";
my $command = <INFILE>;
close INFILE;
chomp $command;

$command =~ s/\$file/ThisFileDoesNotExist/g;
system("$command");
