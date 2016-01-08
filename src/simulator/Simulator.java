package simulator;

import simulator.gui.MainFrame;

public final class Simulator{

    public static void main(String[] args) {
        new MainFrame();
    }

}



// Simulation
//      1. удален метод stop() так как выполняют одно и тоже с pause()
//      2. переименован метод continueSimulation() в start()
//      3. добавлены поля для хранения ссылок на объекты симуляции (роботы, цели, поле)
//      4. execute()

// Configuration
//      1. больше не хранит ссылки на объекты (роботы, цели, поле, симуляция)
