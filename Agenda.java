/***
 * The Agenda is a monitor that handles how the quests are managed, 
 * specifically the maintenance, acquisition, releasing, and completion
 * of quests along with being able to modify quests into the appropriate 
 * agendas
 * 
 * 
 * @author Abhisha Nirmalathas
 * Student Number: 913405
 *
 */
import java.util.ArrayList;

public class Agenda {
	// Name of the agenda
	private String name;
	
	// A queue of quests maintaining completed and new quests
	private ArrayList<Quest> queue;
	public Agenda(String newAgendaName) {
		this.name = newAgendaName;
		this.queue = new ArrayList<Quest>();
	}
	// Adds a new quest to the queue
	public synchronized void addNew(Quest newQuest) {
		while (queue.contains(newQuest) == false) {
			queue.add(newQuest);
			System.out.format("%s added to %s.\n", newQuest.toString(),
					this.name);
			notifyAll();
		}
		
	}
	// Removes a quest from the queue
	public synchronized void removeComplete() {
		while (!queue.isEmpty()) {
			Quest removedQuest = queue.remove(0);
			System.out.format("%s removed from %s.\n", removedQuest.toString(),
					this.name);
			notifyAll();
		}
	}
	/* Knight acquires a new quest during a meeting, while seated 
	 and there is a new quest available*/
	public synchronized void acquire(Knight knight, Hall greatHall) {
		while (queue.isEmpty() || !knight.isSeated || 
				!greatHall.meetingInProgress) {
			try {
				wait();
			}
			catch (InterruptedException e) {}
		}
		Quest newQuest = queue.remove(0);
		knight.addQuest(newQuest);
		System.out.format("%s acquires %s.\n", knight.toString(),
				newQuest.toString());
		notifyAll();
	}
	// Knight sets off to complete a quest after exited from the GreatHall
	public synchronized void setsOff(Knight knight) {
		while (!knight.isOutside || knight.getQuest() == null || 
				knight.getQuest().completed || knight.questing) {
			try {
				wait();
			}
			catch (InterruptedException e) {}
		}
		System.out.format("%s sets off to complete %s!\n", knight.toString(),
				knight.getQuest().toString());
		knight.questing = true;
		notifyAll();
	}
	// Knight completes a quest
	public synchronized void completes(Knight knight) {
		while (!knight.isOutside || knight.getQuest() == null ||
				knight.getQuest().completed || !knight.questing) {
			try {
				wait();
			}
			catch (InterruptedException e) {}
		}
		System.out.format("%s completes %s!\n", knight.toString(), 
				knight.getQuest().toString());
		knight.questing = false;
		knight.getQuest().completed = true;
		notifyAll();
	}
	// Knight reviews and releases a completed quest during a meeting
	public synchronized void releases(Knight knight, Hall greatHall) {;
		while (knight.isOutside || knight.getQuest() == null || 
				!knight.getQuest().completed || !Hall.meetingInProgress) {
			try {
				wait();
			}
			catch (InterruptedException e) {}
		}
		System.out.format("%s releases %s.\n", knight.toString(),
				knight.getQuest().toString());
		queue.add(knight.getQuest());
		knight.releaseQuest(knight.getQuest());
		notifyAll();
	}
}
