#!/usr/bin/perl

use File::Spec;

if (scalar(@ARGV) == 0)
{
	print "Usage: class-patch.pl <bug-number> <java class regexp>\n";
	print "Generates a patch file in the current directory.  The patch file should\n";
	print "be installed in /opt/ops/deploy/platform/CURRENT/lib\n";
	print "in deployed environments to take effect.\n";
	print "\n";
	print "Inner classes are automatically included - you do not need to specify them.\n";
	print "\n";
	print "Examples:\n";
	print "  class-patch.pl 36502 com.enjoyf.platform.service.service.ServiceFinderOne\n";
	print "  class-patch.pl 36799 \"com.enjoyf.platform.service.service.Service*\" com.enjoyf.platform.util.log.GAlerterLogger\n";
	exit;
}

$jar="/opt/ops/tools/linux/jdk/bin/jar";

$viewRoot = `pwd`;
$position = rindex($viewRoot,"/");
$buildDir = substr($viewRoot, 0, $position)."/build/work/patch";

$classRoot = File::Spec->catfile("$buildDir", "classes");
$pwd = `pwd`;
chomp $pwd;

# Enforce starting with a digit.  This has two purposes:
#  - ensure alphabetic ordering sorts patches before platform.jar in the classpath
#  - nag patch creators to associate with bug numbers for tracking purposes

$bug = shift(@ARGV);
if ($bug !~ /^\d/)
{
	die "bug number '$bug' doesn't start with a digit";
}

$decreaseTime = (21474836470 - time());
$jarfile = "$pwd/$decreaseTime-$bug-patch.jar";
unlink $jarfile;
chdir $classRoot;
$first = 1;

# Read off each class file pattern and generate the matching classes with ls.
# While it would be possible to do this entirely in perl, that might not be a
# great idea, since Windows portability could potentially result in weird
# class incompatibility problems.  ls is also simpler.

while ($class = shift(@ARGV))
{
	$class =~ s/\$/\\\$/g;							# backslash pesky $ chars
	($filePattern = $class) =~ s/\./\//g;			# dots become dirseps

	while (glob "$filePattern.class $filePattern\$*.class")
	{
		$file = $_;
		$file =~ s/\$/\\\$/g;						# backslash pesky $ chars

		if (! -e)									# oddly, the -e check does not want $ backslashed
		{
			print "WARNING: $_ did not match any compiled files\n";
			next;
		}

		if (-M > .0416)								# one hour in fractional days
		{
			print "WARNING: $_ is older than one hour - are you sure this is the class file you want?\n";
		}

		print `ls -l $file`;						# show what's being included with dates

		$mode = $first ? "cf" : "uf";
		$first = 0;

		$cmd = "$jar $mode $jarfile $file";
		`$cmd`;
		die "jar failed unexpectedly" if $?;
	}

}

if ($first)
{
	print "patch jar empty\n";
}
