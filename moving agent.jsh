void printBoard(String[][] agents){
    for(int i = 0; i < agents.length; i++){
        for (int j = 0; j < agents[0].length; j++){
            System.out.print(agents[i][j]);
        }
        System.out.println();
    }
}



String[][] createBoard(int size, int popX, int popO){

    String[][] board = new String[size][size];

    for(int i = 0; i < size; i++) {
        for (int j = 0; j < size; j++) {
            board[i][j] = "_";
        }
    }
    while (popX > 0){
        int x = (int)(Math.random()*size);
        int y = (int)(Math.random()*size);
        if (board[x][y].equals("_")){
            board[x][y] = "X";
            popX --;
        }
    }
    while (popO > 0){
        int x = (int)(Math.random()*size);
        int y = (int)(Math.random()*size);
        if (board[x][y].equals("_")){
            board[x][y] = "O";
            popO --;
        }
    }
    return board;
}



boolean isSatisfied(String[][] agents, int row, int col, double t){
    String agent = agents[row][col];
    double x = 0;
    double o = 0;
    for (int i = row-1; i <= row+1; i++){
        for (int j = col-1; j <= col+1; j++){
            if (i < 0 || i >= agents.length || j < 0 || j >= agents[0].length){
                continue;
            }
            if (agents[i][j].equals("X")){
                x ++;
            }
            if (agents[i][j].equals("O")){
                o ++;
            }
        }
    }
    double ratio = 0.0;
    if (agent.equals("X")){
        x --;
        ratio = x / (x + o);
    }
    if (agent.equals("O")){
        o --;
        ratio = o/(x + o);
    }
    if (x + o == 0){
        return true;
    }

    if (ratio >= t){
        return true;
    } else {
        return false;
    }

}



void moveAgentRandom(String[][] agents, int row, int col){
    int i = (int)(Math.random()*(agents.length));
    int j = (int)(Math.random()*(agents[0].length));
    while (! agents[i][j].equals("_")){
        i = (int)(Math.random()*(agents.length));
        j = (int)(Math.random()*(agents[0].length));
    }
    agents[i][j] = agents[row][col];
    agents[row][col] = "_";
}



void moveAgentClose(String[][] agents, int row, int col){
    int round = 1;
    boolean b = false;
    while (b != true) {
        for (int i = row - round; i <= row + round; i++) {
            for (int j = col - round; j <= col - round; j++) {
                if (i < 0 || i >= agents.length || j < 0 || j >= agents[0].length){
                    continue;
                }
                if (agents[i][j].equals("_")) {
                    agents[i][j] = agents[row][col];
                    agents[row][col] = "_";
                    b = true;
                }
            }
        }
        round++;
        if (round == agents.length - 1){
            System.out.println("Unable to relocate agent.");
            return;
        }
    }
}



boolean allSatisfied(String[][] agents, double t){
    boolean b = true;
    for(int i = 0; i < agents.length; i++) {
        for (int j = 0; j < agents[0].length; j++) {
            if (agents[i][j].equals("_")){
                continue;
            }
            boolean c = isSatisfied(agents, i, j, t);
            b = b && c;
        }
    }
    return b;
}



int simulateRandom(){
    Scanner sc = new Scanner(System.in);
    System.out.print("Please enter a length for the board:");
    int size = sc.nextInt();
    System.out.print("Please enter the number of X agents:");
    int x = sc.nextInt();
    System.out.print("Please enter the number of O agents:");
    int o = sc.nextInt();
    String[][] board = createBoard(size,x,o);
    System.out.print("Please enter the threshold for satisfaction:");
    double t = sc.nextDouble();
    boolean result = allSatisfied(board,t);
    int round = 0;
    while (result != true){
        for(int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j].equals("_")){
                    continue;
                }
                if (!isSatisfied(board, i, j, t)) {
                    moveAgentRandom(board, i, j);
                }
            }
        }
        System.out.println();
        round ++;
        printBoard(board);
        result = allSatisfied(board,t);
        if (round > size*size*size){
            System.out.println(">>> Unable to generate all-satified configuration.");
            return round;
        }
    }
    return round;
}



int simulateClosest(){
    Scanner sc = new Scanner(System.in);
    System.out.print("Please enter a length for the board:");
    int size = sc.nextInt();
    System.out.print("Please enter the number of X agents:");
    int x = sc.nextInt();
    System.out.print("Please enter the number of O agents:");
    int o = sc.nextInt();
    String[][] board = createBoard(size,x,o);
    System.out.print("Please enter the threshold for satisfaction:");
    double t = sc.nextDouble();
    boolean result = allSatisfied(board,t);
    int round = 0;
    while (result != true) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j].equals("_")) {
                    continue;
                }
                if (!isSatisfied(board, i, j, t)) {
                    moveAgentClose(board, i, j);
                }
            }
        }
        System.out.println();
        round++;
        printBoard(board);
        result = allSatisfied(board, t);
        if (round > size*size*size){
            System.out.println(">>> Unable to generate all-satified configuration.");
            return round;
        }
    }
    return round;
}