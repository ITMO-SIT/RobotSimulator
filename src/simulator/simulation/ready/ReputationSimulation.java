package simulator.simulation.ready;


import simulator.robot.Robot;
import simulator.services.Configuration;
import simulator.robot.ReputationRobot;
import simulator.simulation.Simulation;
import simulator.target.DefaultTarget;
import simulator.target.Target;

import java.util.HashMap;
import java.util.Random;

public class ReputationSimulation extends Simulation<ReputationRobot> {

    public double[][] D;
    public int[][] V;
    public int[][] S;
    public double[] W;
    public double[] P;
    public double[] Q;
    public double[] L;
    public double[][] H;
    public double[][] G;
    public int N = 8, M = 2, diversionists = 2;
    public double limreputation = 0.5, activeDist = 50;

    private double F(double sec, double t) {
        return 1 - Math.pow(Math.E, -sec * t);
    }


    @Override
    public void init() {

        double a = 0.6, t = 1;
        D = new double[N][M];
        V = new int[N][N];
        S = new int[N][N];
        W = new double[N];
        P = new double[N];
        L = new double[N];
        Q = new double[N];
        H = new double[N][N];
        G = new double[N][N];
        Configuration conf = Configuration.getInstance();
        Random randPosition = new Random();
        DefaultTarget target;
        ReputationRobot robot;
        for (int i = 0; i < M; i++) {
            target = (DefaultTarget)conf.newTargetInstance();
            int X, Y, count = 0;
            while (true) {
                count++;
                X = randPosition.nextInt(N * 10);
                Y = randPosition.nextInt(N * 10);
                if (!checkRobotXY(X, Y) || count == N) break;
            }
            target.setX(X);
            target.setY(Y);
            target.setSize(4);
            targets.add(target);
        }

        for (int i = 0; i < N; i++) {
            robot = (ReputationRobot)conf.newRobotInstance();
            int X, Y, count = 0;
            while (true) {
                X = randPosition.nextInt(N * 10);
                Y = randPosition.nextInt(N * 10);
                if (!checkRobotXY(X, Y) || ++count == N) break;
            }
            robot.setX(X);
            robot.setY(Y);
            robot.setActiveDist(activeDist);
            robot.setId(i);
            robot.setSimulation(this);
            targets.forEach(robot :: addTarget);
            robots.add(robot);
        }

        for (int i = 0; i < diversionists; i++) {
            while (true) {
                int rand = randPosition.nextInt(N);
                if (robots.get(rand).isGoodBoy()) {
                    robots.get(rand).setGoodBoy(false);
                    break;
                }
            }
        }

        for (int i = 0; i < N; i++) {
            D[i] = robots.get(i).calcTargetsDistance();
        }

        for (int i = 0; i < N; i++) {
            robots.get(i).findNeighbors();
            robots.get(i).checkNeighbors();
        }

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < i; j++) {
                if (V[i][j] == V[j][i] && V[i][j] == 1) {
                        S[i][j]++;
                        S[j][i]++;
                }
                if (V[i][j] == V[j][i] && V[i][j] == -1) {
                    S[i][j]--;
                    S[j][i]--;
                }
            }
        }

        for(int i = 0; i < N; i++) {
            for (int j = i + 1; j < N; j++) {
                for (int k = 0; k < N; k++) {
                    if (i == k || j == k) {
                        continue;
                    }
                    if (V[i][k] != 0 && V[j][k] != 0) {
                        if (V[i][k] == V[j][k]) {
                            S[i][j]++;
                            S[j][i]++;
                        }
                        else {
                            S[i][j]--;
                            S[j][i]--;
                        }
                    }
                }
            }
        }

        for (int i = 0; i < N; i++) {
            double plus, minus;
            plus = 0;
            minus = 0;
            for (int j = 0; j < N; j++) {
                if (S[i][j] > 0) {
                    plus += S[i][j];
                }
                else {
                    minus += Math.abs(S[i][j]);
                }
            }
            Q[i] = plus / (plus + minus);
        }

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (V[i][j] == 1) {
                    H[i][j] = 1;
                }
                else {
                    H[i][j] = 0;
                }
                if (V[i][j] == -1) {
                    G[i][j] = 1;
                }
                else {
                    G[i][j] = 0;
                }
            }
        }

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                P[i] += H[i][j] * Q[j] * F(a, t);
                L[i] += G[i][j] * Q[j] * F(a, t);
            }
        }

        for (int i = 0; i < N; i++) {
            W[i] = P[i] / (P[i] + L[i]);
            if (W[i] > limreputation) {
                System.out.println("Робот №" + i + " - легитимный агент");
            }
            else {
                System.out.println("Робот №" + i + " - диверсант");
            }
        }

    }


    @Override
    public void nextIteration() {

    }

    @Override
    public void setSimulationParam(HashMap<String, String> param) {
        super.setSimulationParam(param);
        if (param.get("number_of_robots") != null) N = Integer.parseInt(param.get("number_of_robots"));
        if (param.get("number_of_targets") != null) M = Integer.parseInt(param.get("number_of_targets"));
        if (param.get("number_of_diversionists") != null) diversionists = Integer.parseInt(param.get("number_of_diversionists"));
        if (param.get("limit_reputation") != null) limreputation = Double.parseDouble(param.get("limit_reputation"));
        if (param.get("active_distance") != null) activeDist = Double.parseDouble(param.get("active_distance"));
    }

    private boolean checkRobotXY(int X, int Y) {
        for (Robot robot : robots)
            if (robot.getX() == X && robot.getY() == Y)
                return true;
        for (Target target : targets)
            if (target.getX() == X && target.getY() == Y)
                return true;
        return false;
    }
}
