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
