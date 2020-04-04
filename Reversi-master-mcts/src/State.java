
import java.util.*;


public class State {
    private HashMap<Integer, Integer> actionSet = null;
    private HashMap<Integer, Integer> dirSet = null;
    private boolean terminal = false;
    private boolean isMachine = false;
    private int Chesser;
    private int numBlack = 2;
    private int numWhite = 2;
    private int leadAction = -1;
    
  
    private int[][] state = {
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, Constants.WHITE, Constants.BLACK, 0, 0, 0},
            {0, 0, 0, Constants.BLACK, Constants.WHITE, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
    };

    public State(boolean isMachine, int Chesser) {
        this.isMachine = isMachine;
        this.Chesser = Chesser;
        actionSet = new HashMap<Integer, Integer>();
        dirSet = new HashMap<Integer, Integer>();
        clearActionSet();
    }
    public State(int[][] s, boolean isMachine, int Chesser) {
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                state[i][j] = s[i][j];
            }
        }
        this.isMachine = isMachine;
        this.Chesser = Chesser;
        actionSet = new HashMap<Integer, Integer>();
        dirSet = new HashMap<Integer, Integer>();
        clearActionSet();
    }
    public void changePlayer() {
    	Chesser = -Chesser;
        isMachine = !isMachine;
        clearActionSet();
        
    }
    public void clearActionSet() {
        actionSet.clear();
        dirSet.clear();
      
        Judge();
    }
    public void changeState(int action) {
        int dir = dirSet.get(action);
        int i = action / 8;
        int j = action % 8;
        int m = i;
        int n = j;
        leadAction = action;
        state[i][j] = Chesser;
        int cnt = 1;
        if((dir & Constants.NORTH) > 0) {
            --m;
            while(state[m][n] + Chesser == 0) {
                state[m--][n] = Chesser;
                cnt++;
            }
            //state[m][n] = Chess;
        }
        m = i;
        n = j;
        if((dir & Constants.NORTHEAST) > 0) {
            --m;
            ++n;
            while(state[m][n] + Chesser == 0) {
                state[m--][n++] = Chesser;
                cnt++;
            }
            //state[m][n] = Chesser;
        }
        m = i;
        n = j;
        if((dir & Constants.EAST) > 0) {
            ++n;
            while(state[m][n] + Chesser == 0) {
                state[m][n++] = Chesser;
                cnt++;
            }
            //state[m][n] = Chesser;
        }
        m = i;
        n = j;
        if((dir & Constants.SOUTHEAST) > 0) {
            ++m;
            ++n;
            while(state[m][n] + Chesser == 0) {
                state[m++][n++] = Chesser;
                cnt++;
            }
            //state[m][n] = Chesser;
        }
        m = i;
        n = j;
        if((dir & Constants.SOUTH) > 0) {
            ++m;
            while(state[m][n] + Chesser == 0) {
                state[m++][n] = Chesser;
                cnt++;
            }
            //state[m][n] = Chesser;
        }
        m = i;
        n = j;
        if((dir & Constants.SOUTHWEST) > 0) {
            ++m;
            --n;
            while(state[m][n] + Chesser == 0) {
                state[m++][n--] = Chesser;
                cnt++;
            }
            //state[m][n] = Chesser;
        }
        m = i;
        n = j;
        if((dir & Constants.WEST) > 0) {
            --n;
            while(state[m][n] + Chesser == 0) {
                state[m][n--] = Chesser;
                cnt++;
            }
            //state[m][n] = Chesser;
        }
        m = i;
        n = j;
        if((dir & Constants.NORTHWEST) > 0) {
            --m;
            --n;
            while(state[m][n] + Chesser == 0) {
                state[m--][n--] = Chesser;
                cnt++;
            }
            //state[m][n] = Chesser;
        }
        if(Chesser == Constants.BLACK) {
            numBlack += cnt;
        }
        else {
            numWhite += cnt;
        }
    }
    public HashMap<Integer, Integer> getActionSet() {
        return actionSet;
    }
    public int getRandomAction() {
        int size = actionSet.size();
        int i = 0;
        int maxIndex = -1;
        int maxReward = -10000;
        if(size == 0) {
            return -1;
        }
        if(actionSet.containsValue(0)) {
            return 0;
        }
        if(actionSet.containsValue(7)) {
            return 7;
        }
        if(actionSet.containsValue(56)) {
            return 56;
        }
        if(actionSet.containsValue(63)) {
            return 63;
        }
        if(numBlack + numWhite > 64) {
            int cnt = 3;
            int sel = -1;
            while(cnt > 0) {
                cnt--;
                sel = Constants.random.nextInt(1024) % size;
                int row = actionSet.get(sel) / 8;
                int col = actionSet.get(sel) % 8;
                if((state[0][0] != Chesser && row < 2 && col < 2) || (state[7][0] != Chesser && row > 5 && col < 2) || (state[7][7] != Chesser && row > 5 && col > 5) || (state[0][7] != Chesser && row < 2 && col > 5)) {
                    continue;
                }
                break;
            }
            return actionSet.get(sel);
        }
        State tmp = this.clone();
        int W = getWeight(this.getState(), Chesser);
        ArrayList<Integer> list = new ArrayList<Integer>();
        int min = 10000;
        while (i < size) {
            Integer a = actionSet.get(i);
            tmp.changeState(a);
            int w = getWeight(tmp.getState(), Chesser);
            int d = w - W ;
            list.add(d);
            if(min > d) {
                min = d;
            }
            if(d > maxReward) {
                maxReward = d;
                maxIndex = i;
            }
            tmp.Reverse(this);
            i++;
        }
        return actionSet.get(maxIndex);
       
    }
    private int findActionByDirection(int dir, int m, int n) {
        int first = -1;
        boolean flag = false;
        while(true) {
            if(dir == Constants.NORTH) {
                m--;
            }
            else if(dir == Constants.NORTHEAST) {
                m--;
                n++;
            }
            else if(dir == Constants.EAST) {
                n++;
            }
            else if(dir == Constants.SOUTHEAST) {
                m++;
                n++;
            }
            else if(dir == Constants.SOUTH) {
                m++;
            }
            else if(dir == Constants.SOUTHWEST) {
                m++;
                n--;
            }
            else if(dir == Constants.WEST) {
                n--;
            }
            else if(dir == Constants.NORTHWEST) {
                m--;
                n--;
            }
            if(m < 0 || m > 7 || n < 0 || n > 7) {
                break;
            }
            if(!flag) {
                if(state[m][n] == 0) {
                    return 0;
                }
                first = state[m][n];
                flag = true;
            }
            else {
                if(state[m][n] + first == 0 && state[m][n] == Chesser) {
                    return 2;
                }
                else if(state[m][n] + first == 0 && state[m][n] == -Chesser) {
                    return 1;
                }
                else if(state[m][n] == 0) {
                    return 0;
                }
            }
        }
        return 0;
    }
    public void Judge() {
        terminal = true;
        int no = 0;
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                if(state[i][j] == 0) {
                    int action = i * 8 + j;
                    boolean flag = false;
                    for(int dir = Constants.NORTH; dir <= Constants.NORTHWEST; dir <<= 1) {
                        int res = findActionByDirection(dir, i, j);
                        if(res == 0) {
                            continue;
                        }
                        if(res == 2) {
                            action |= dir;
                            flag = true;
                        }
                        terminal = false;
                    }
                    if(flag) {
                        actionSet.put(no, action & Constants.Position);
                        dirSet.put(action & Constants.Position, action & Constants.Direction);
                        no++;
                    }
                }
            }
        }
    }
    public boolean isTerminal() {
        return terminal;
    }
    public int getWinner() {
    
        return numBlack > numWhite ? Constants.BLACK : (numBlack == numWhite ? 0 : Constants.WHITE);
    }
    public int[][] getState() {
        return state.clone();
    }
    public boolean getIsMatchine() {
        return isMachine;
    }
    public int getChesser() {
        return Chesser;
    }
    public State clone() {
        State s = new State(this.state, this.isMachine, this.Chesser);
        s.terminal = this.terminal;
        if(this.actionSet != null) {
            s.actionSet = (HashMap<Integer, Integer>) this.actionSet.clone();
        }
        if(this.dirSet != null) {
            s.dirSet = (HashMap<Integer, Integer>) this.dirSet.clone();
        }
        s.leadAction = this.leadAction;
        s.numWhite = this.numWhite;
        s.numBlack = this.numBlack;
        return s;
    }
    public void Display() {
        System.out.printf("%4d", 0);
        for(int i = 1; i < 8; i++) {
            System.out.printf("%3d", i);
        }
        System.out.println();
        for(int i = 0; i < 8; i++) {
            System.out.print(i);
            for(int j = 0; j < 8; j++) {
                System.out.printf("%3d", getState()[i][j]);
            }
            System.out.println();
        }
    }
    public void setLeadAction(int a) {
        leadAction = a;
    }
    public int getLeadAction() {
        return leadAction;
    }
    public HashMap<Integer, Integer> getDirSet() {
        return dirSet;
    }
    private void Reverse(State last) {
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                state[i][j] = last.getState()[i][j];
            }
        }
        this.isMachine = last.getIsMatchine();
        this.Chesser = last.getChesser();
        this.numBlack = last.numBlack;
        this.numWhite = last.numWhite;
        this.leadAction = last.leadAction;
        //genActionSet();
    }
    private int getWeight(int[][] state, int Chesser) {
        int w = 0;
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                if(state[i][j] == Chesser) {
                    w += Constants.weightMatrix[i][j];
                }
            }
        }
        return w;
    }
}
