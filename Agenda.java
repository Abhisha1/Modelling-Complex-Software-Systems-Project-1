import java.util.ArrayList;

public class Agenda {
	private String name;
	private ArrayList<Quest> queue;
	public Agenda(String newAgendaName) {
		this.name = newAgendaName;
		this.queue = new ArrayList<Quest>();
	}
	public synchronized void addNew(Quest newQuest) {
		while (queue.contains(newQuest) == false) {
			queue.add(newQuest);
			System.out.format("%s added to %s\n", newQuest.toString(), this.name);
			notifyAll();
		}
		
	}
	public synchronized void removeComplete() {
		while (!queue.isEmpty()) {
			Quest removedQuest = queue.remove(0);
			System.out.format("%s removed from to %s\n", removedQuest.toString(), this.name);
			notifyAll();
		}
	}
	
	public synchronized void acquire(Knight knight, Hall greatHall) {
		while (queue.isEmpty() || !knight.isKnightSeated() || !greatHall.meetingInProgress) {
			try {
				wait();
			}
			catch (InterruptedException e) {}
		}
		Quest newQuest = queue.remove(0);
		knight.addQuest(newQuest);
		System.out.format("%s acquires %s\n", knight.toString(), newQuest.toString());
		notifyAll();
	}
	public synchronized void setsOff(Knight knight) {
		while (!knight.isKnightOutside() || knight.getQuest() == null || knight.getQuest().completed) {
			try {
				wait();
			}
			catch (InterruptedException e) {}
		}
		System.out.format("%s sets off to complete %s!\n", knight.toString(), knight.getQuest().toString());
		notifyAll();
	}
	public synchronized void completes(Knight knight) {
		while (!knight.isKnightOutside() || knight.getQuest() == null || knight.getQuest().completed) {
			try {
				wait();
			}
			catch (InterruptedException e) {}
		}
		System.out.format("%s completes %s!\n", knight.toString(), knight.getQuest().toString());
		knight.getQuest().completed = true;
		notifyAll();
	}
	public synchronized void releases(Knight knight, Hall greatHall) {
		while (!knight.isKnightOutside() || knight.getQuest() == null || !knight.getQuest().completed || greatHall.meetingInProgress) {
			try {
				wait();
			}
			catch (InterruptedException e) {}
		}
		System.out.format("%s releases %s.\n", knight.toString(), knight.getQuest().toString());
		queue.add(knight.getQuest());
		knight.releaseQuest(knight.getQuest());
		notifyAll();
	}
}
