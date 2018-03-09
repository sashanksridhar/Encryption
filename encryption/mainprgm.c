#include<stdio.h>
#include<unistd.h>
int main()
{
int ch;
printf("\n enter 1. for text 2. for audio 3. for video");
scanf("%d",&ch);
switch(ch)
{
case 1:
{
char *a[3] = {"java","encryption",NULL};
execvp(*a,a);
}
case 2:
{
char *a[3] = {"java","audioencryption",NULL};
execvp(*a,a);
}
case 3:
{
char *a[3] = {"java","videoencryption",NULL};
execvp(*a,a);
}

}
}
