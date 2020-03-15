
public class KingArthur extends Thread{
	// Checks whether King Arthur is inside the GreatHall or not
	public volatile boolean isOutside = true;
	
	private Hall greatHall;
	
	public KingArthur(Hall greatHall) {
		this.greatHall = greatHall;
	}
	
	 public void run() {
		 while (!isInterrupted()) {
            try {
				 while (true) {
					 if (this.isOutside) {
						 this.greatHall.kingEnter(this);
						 this.greatHall.commenceMeeting(this);
						 this.greatHall.endMeeting(this);
						 this.greatHall.KingExit(this);
						 // King waits before re-entering the Great hall
						 sleep(Params.getKingWaitingTime());
					 }
				 }
            }
            catch (InterruptedException e) {
                this.interrupt();
            }
		 }
	 }
	 
	
}
