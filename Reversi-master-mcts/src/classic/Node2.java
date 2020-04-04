package classic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Node2 {
	private static double uctArg = 1.414213;
    private State state;
    private Node2 parent;
    List<Node2> children;
    int visitNum;
    int winScore;
    private int evaScore;

    private static int WIN_SCORE = 1;
    private static int interval = 5000;

    Node2(State state) {
        this.state = state;
        evaScore = state.evaluate();
    }

    int[] nextMove () {
        long beginTime = System.currentTimeMillis();
        int playout = 0;
        while (System.currentTimeMillis() < beginTime + interval) {

        	playout++;
            // 1. select
            Node2 curNode = select();
            // 2. expand
            curNode.expand();
            // 3. simulation
            if ( !curNode.state.gameOver() ) {
                curNode = curNode.getRandomChild();
            }
            int winner = curNode.simulate();
            // 4. back propagation
            backPropagation(curNode, winner);
        }
        double maxVisitNum = 0;
        int index = 0;
        for (int i = 0; i < children.size(); i++) {
            if ((children.get(i).visitNum>0)&&(children.get(i).winScore*1.0/children.get(i).visitNum > maxVisitNum)) {
                maxVisitNum = children.get(i).winScore*1.0/children.get(i).visitNum;
                index = i;
            }
        }
        System.out.println("playout = " + playout+";;;;;time="+(System.currentTimeMillis()- beginTime)/1000.0);
        return this.state.possibleMoves().get(index);
    }

    private Node2 select () {
        Node2 curNode = this;
        while (curNode.children != null && curNode.children.size() > 0) {
            curNode = findBestNodeWithUCT(curNode);
        }
        return curNode;
    }

    private void expand () {
        List<int[]> moves = this.state.possibleMoves();
        this.children = new ArrayList<>();
        for (int[] move : moves) {
            State curState = this.state.clone();
           
                if(curState.execMove(move[0],move[1]));
            {
            	 Node2 childNode = new Node2(curState);
                 childNode.parent = this;
                 this.children.add(childNode);
            }
           
        }
    }

    private Node2 getRandomChild () {
        int count = (int) ( Math.random() * children.size() );
        return children.get(count);
    }

    private int simulate () {
        Node2 tempNode = new Node2(this.state.clone());
        while (!tempNode.state.gameOver()) {
            tempNode.expand();
          
            tempNode = tempNode.getRandomChild();
        }
        return tempNode.state.currWinner();
    }

    private void backPropagation(Node2 node, int winner) {
        Node2 temp = node;
        while (temp != null) {
            temp.visitNum++;
            if (winner != temp.state.getCurrPlayer()) {
                temp.winScore += WIN_SCORE;
            }
            temp = temp.parent;
        }
    }
    private static double uctValue (int totalVisit, double nodeWinScore, int nodeVisit) {
        if (nodeVisit == 0) {
            return Integer.MAX_VALUE;
        }
        return ( nodeWinScore / (double) nodeVisit )
                + uctArg * Math.sqrt(Math.log(totalVisit) / (double) nodeVisit);
    }

    static Node2 findBestNodeWithUCT(Node2 node) {
        int parentVisit = node.visitNum;
        return Collections.max(
                node.children,
                Comparator.comparing(c -> uctValue(parentVisit, c.winScore, c.visitNum))
        );
    }

}
