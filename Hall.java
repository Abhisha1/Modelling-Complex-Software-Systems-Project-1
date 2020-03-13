
public class Hall {
private String name;
	
	private Agenda agendaNew;
	
	private Agenda agendaComplete;

    public volatile boolean meetingInProgress;

    private volatile boolean kingArthurPresent;
    
    private volatile int nKnightsPresent;
    
    private volatile int nKnightsSeated;
	
	public Hall(String name, Agenda agendaNew, Agenda agendaComplete) {
		this.name = name;
		this.agendaNew = agendaNew;
		this.agendaComplete = agendaComplete;
        this.meetingInProgress = false;
        this.kingArthurPresent = false;
        this.nKnightsPresent = 0;
        this.nKnightsSeated = 0;
	}

    public synchronized void knightEnter(Knight knight){
        while (kingArthurPresent){
            try{
                wait();
            }catch (InterruptedException e){}
        }
        System.out.format("%s enters %s\n", knight.toString(), this.name);
        knight.isOutside = false;
        nKnightsPresent++;
        notifyAll();
    }
    public synchronized void kingEnter(KingArthur king){
        while (kingArthurPresent){
            try{
                wait();
            }catch (InterruptedException e){}
        }
        System.out.format("King Arthur enters %s\n", this.name);
        this.kingArthurPresent = true;
        king.isOutside = false;
        notifyAll();
    }
    public synchronized void knightExit(Knight knight){
        while (kingArthurPresent){
            try{
                wait();
            }catch (InterruptedException e){}
        }
        System.out.format("%s exits %s\n", knight.toString(), this.name);
        knight.isOutside = true;
        this.nKnightsPresent--;
        notifyAll();
    }
    public synchronized void KingExit(KingArthur king){
        while (meetingInProgress){
            try{
                wait();
            }catch (InterruptedException e){}
        }
        System.out.format("King Arthur  exits %s\n", this.name);
        this.kingArthurPresent = false;
        king.isOutside = true;
        notifyAll();
    }
    public synchronized void sit(Knight knight) {
    	while(!kingArthurPresent || knight.isKnightSeated()) {
    		try {
    			wait();
    		}catch (InterruptedException e){}
    	}
    	System.out.format("%s sits at the Round Table.\n", knight.toString());
    	knight.isSeated = true;
    	nKnightsSeated++;
        notifyAll();
    }
    
    public synchronized void commenceMeeting(KingArthur king) {
    	while(king.isOutside || (this.nKnightsPresent != this.nKnightsSeated)) {
    		try {
    			wait();
    		}catch (InterruptedException e){}
    	}
    	System.out.format("# knights pres %d, # seat %d \n", this.nKnightsPresent, this.nKnightsSeated);
    	System.out.format("Meeting begins\n");
    	this.meetingInProgress = true;
    	notifyAll();
    }
    public synchronized void stand(Knight knight) {
    	while(!kingArthurPresent || !knight.isKnightSeated() || !knight.hasQuest()) {
    		try {
    			wait();
    		}catch (InterruptedException e){}
    	}
    	System.out.format("%s stands at the Round Table.\n", knight.toString());
    	knight.isSeated = false;
    	nKnightsSeated--;
        notifyAll();
    }
    public synchronized void endMeeting(KingArthur king) {
    	while(this.nKnightsPresent == 0 || this.nKnightsSeated != 0 || king.isOutside) {
    		try {
    			wait();
    		}catch (InterruptedException e){}
    	}
    	System.out.format("Meeting ends!\n");
    	this.meetingInProgress = false;
    	notifyAll();
    }
}
