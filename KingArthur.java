/***
 * The thread that controls the activities King Arthur performs, including entering,
 * exiting, commencing and concluding meetings.
 * 
 * @author Abhisha Nirmalathas
 * Student Number: 913405
 *
 */
public class KingArthur extends Thread{
	
	private Hall greatHall;
	
	public KingArthur(Hall greatHall) {
		this.greatHall = greatHall;
	}
	
	/* Thread that controls King cyclically entering,
	 *  starting meetings, ending meetings and exiting
	 */
	 public void run() {
		 while (!isInterrupted()) {
            try {
				 this.greatHall.kingEnter(this);
				 this.greatHall.commenceMeeting(this);
				 this.greatHall.endMeeting(this);
				 this.greatHall.KingExit(this);
				 // King waits before re-entering the Great hall
				 sleep(Params.getKingWaitingTime());
            }
            catch (InterruptedException e) {
                this.interrupt();
            }
		 }
	 }
	 
	
}
