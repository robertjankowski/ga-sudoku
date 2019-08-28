# ga-sudoku
Solving sudoku using genetic algorithms

## Build, run and play

#### Run docker app from docker registry
```bash
docker run --net=host \
--env="DISPLAY" \
--volume="$HOME/.Xauthority:/root/.Xauthority:rw" robjankowski/ga-sudoku:0.1
```

Or 
#### Build image and run with docker

```sbtshell
sbt docker:publishLocal
```

```bash
docker run --net=host \
--env="DISPLAY" \
--volume="$HOME/.Xauthority:/root/.Xauthority:rw" ga-sudoku:0.1
```

Or 
#### Just run app
```sbtshell
sbt run
```


**Remember to open your X server**
```bash
xhost +
```


***
On Windows

1. Install [`Xming` server](https://sourceforge.net/projects/xming/)

2. Add your ip to `Xming` server. Find `X0.hosts` file in Xming instalation folder and add your ip below.

3. Set DISPLAY variable (powershell) `set-variable -name DISPLAY -value YOUR_IP:0.0`

4. Run app
```powershell
docker run -it --privileged -e DISPLAY=$DISPLAY \ 
-v /tmp/.X11-unix:/tmp/.X11-unix robjankowski/ga-sudoku:0.1
```
