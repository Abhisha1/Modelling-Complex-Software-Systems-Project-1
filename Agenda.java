import java.util.ArrayList;

public class Agenda {
	private String name;
	private ArrayList<Quest> queue;
	private Agenda(String newAgendaName) {
		this.name = newAgendaName;
		this.queue = new ArrayList<Quest>();
	}
	public synchronized void addNew(Quest newQuest) {
		while (queue.contains(newQuest) == false) {
			queue.add(newQuest);
			System.out.format("%s added to %s", newQuest.toString(), this.name);
			notifyAll();
		}
		
	}
	public synchronized void removeComplete(Quest questToBeRemoved) {
		queue.remove(questToBeRemoved);
		System.out.format("%s removed from to %s", questToBeRemoved.toString(), this.name);
	}
	public synchronized void acquire(Knight currentKnight) {
		while (queue.isEmpty()) {
			try {
				wait();
			}
			catch (InterruptedException e) {
			}
		}
		currentKnight.addQuest(queue.get(0));
		queue.remove(0);
	}
}
