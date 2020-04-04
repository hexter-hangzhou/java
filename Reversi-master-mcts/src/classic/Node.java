package classic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Node {
	private static double uctArg = 1.414213;
    private State state;
    private Node parent;
    List<Node> children;
    int visitNum;
    int winScore;
    private int evaScore;

    private static int WIN_SCORE = 1;
    private static int interval = 5000;

    Node(State state) {
        this.state = state;
        evaScore = state.evaluate();
    }

    int[] nextMove () {
        long beginTime = System.currentTimeMillis();
        int playout = 0;
        while (System.currentTimeMillis() < beginTime + interval) {

        	playout++;
            // 1. select
            Node curNode = select();
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
        int maxVisitNum = 0;
        int index = -1;
        for (int i = 0; i < children.size(); i++) {
            if (children.get(i).visitNum > maxVisitNum) {
                maxVisitNum = children.get(i).visitNum;
                index = i;
            }
        }
        System.out.println("playout = " + playout+";;;;;time="+(System.currentTimeMillis()- beginTime)/1000.0);
        return this.state.possibleMoves().get(index);
    }

    private Node select () {
        Node curNode = this;
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
            	 Node childNode = new Node(curState);
                 childNode.parent = this;
                 this.children.add(childNode);
            }
           
        }
    }

    private Node getRandomChild () {
        int count = (int) ( Math.random() * children.size() );
        return children.get(count);
    }

    private int simulate () {
        Node tempNode = new Node(this.state.clone());
        while (!tempNode.state.gameOver()) {
            tempNode.expand();
            if (Math.random() < 0.5) {
                int maxScore = Collections.max(tempNode.children, Comparator.comparing(child -> child.evaScore)).evaScore;
                tempNode.children = tempNode.children.stream()
                        .filter(child -> child.evaScore == maxScore)
                        .collect(Collectors.toList());
            }
            tempNode = tempNode.getRandomChild();
        }
        return tempNode.state.currWinner();
    }

    private void backPropagation(Node node, int winner) {
        Node temp = node;
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

    static Node findBestNodeWithUCT(Node node) {
        int parentVisit = node.visitNum;
        return Collections.max(
                node.children,
                Comparator.comparing(c -> uctValue(parentVisit, c.winScore, c.visitNum))
        );
    }

}
