/*
 * QueryGoogleCalendarCapability.java 1.0 12/06/18
 */
package edu.cmu.gizmo.management.capability;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

import com.google.gdata.client.calendar.CalendarQuery;
import com.google.gdata.client.calendar.CalendarService;
import com.google.gdata.data.DateTime;
import com.google.gdata.data.calendar.CalendarEventEntry;
import com.google.gdata.data.calendar.CalendarEventFeed;
import com.google.gdata.data.calendar.EventWho;
import com.google.gdata.data.extensions.Where;
import com.google.gdata.util.ServiceException;

/**
 * This class is the capability used to query a google calendar for a person's
 * availability
 * 
 * @version 1.0 18 Jun 2012
 * @author Arpita Shrivastava
 */
public class QueryGoogleCalendarCapability extends Capability {

	/** The human-readable description for this capability */
	private final String DESCRIPTION = "Query Google calendar for availability";

	/** The human-readable description for this capability */
	private final String NAME = "QueryGoogleCalendarCapability";

	/** Person name is a requirement for this capability */
	private final String PERSON_NAME = "personName";

	private final String OUTPUT_ROOM_NUMBER = "roomnumber";
	
	private final String START_TIME = "startTime";

	private final String END_TIME = "endTime";

	private final String AVAILABILITY_STATUS = "status";

	private final String ROOM_NUMBER = "room";

	private final String PERSONNEL_STORE = "personneldata";

	private final String PERSON_FULL_NAME = "person";

	private String personName;
	private String startTime;
	private String endTime;
	private String roomNumber;
	private Boolean availStatus;
	private String personFullName;
	private String personEMail;
	private boolean newInputFromUser;

	public QueryGoogleCalendarCapability() {
		personName = null;
		startTime = null;
		endTime = null;
	}

	public String getPersonFullName() {
		return personFullName;
	}

	public String getRoomNumber() {
		return roomNumber;
	}

	public Boolean getAvailStatus() {
		return availStatus;
	}

	@Override
	public void execute() {

		while (true) {

			if (newInputFromUser == false) {
				try {
					Thread.sleep(100);
				} catch (final InterruptedException e) {
				}
			} else {
				newInputFromUser = false;
				final CalendarService myService = new CalendarService("example");
				URL feedUrl;
				try {
					final ArrayList<String> persData = readPersonnelData();
					for (final String persDetails : persData) {
						final String[] persFields = persDetails.split(",");
						if (persFields[0].contains(personName)) {
							personFullName = persFields[0];
							personEMail = persFields[1];
							roomNumber = persFields[2];
							break;
						}
					}

					// If the person is found, look up the calendar
					if (personFullName != null) {

						// Feed URL for MSE Google calendar
						feedUrl = new URL(
								"https://www.google.com/calendar/feeds/mse.team.gizmo%40gmail.com/private-4bdbc889dec68a0ea5e357f04328e987/full");

						final CalendarQuery myQuery = new CalendarQuery(feedUrl);
						myQuery.setMinimumStartTime(DateTime
								.parseDateTime(startTime));
						myQuery.setMaximumStartTime(DateTime
								.parseDateTime(endTime));

						// Send the request and receive the response:
						final CalendarEventFeed resultFeed = myService.query(
								myQuery, CalendarEventFeed.class);

						// If no events are found for that time period, set
						// availStatus to true.
						// roomNumber will remain as the default office location
						// set before.
						if (resultFeed.getEntries().size() <= 0) {
							availStatus = true;
							sendQueryResults();
							setStatus(CapabilityStatus.COMPLETE,
									"QueryCalendar complete");
							return;
						} else {
							List<EventWho> participants;

							// Get all the calendar events for the time period
							for (int i = 0; i < resultFeed.getEntries().size(); i++) {
								final CalendarEventEntry entry = resultFeed
										.getEntries().get(i);
								participants = entry.getParticipants();

								// Check if the person is one of the
								// participants in the event
								for (final EventWho participant : participants) {
									if (personEMail.equals(participant
											.getEmail().toString())) {

										// If the person is a participant in the
										// event, set availability to false
										availStatus = false;
										final List<Where> loc = entry
												.getLocations();

										// If a room number is available for the
										// event, set roomNumber to that room
										// number
										if (loc.get(0).getValueString() != null) {
											roomNumber = loc.get(0)
													.getValueString();

											sendQueryResults();
											setStatus(
													CapabilityStatus.COMPLETE,
													"QueryCalendar complete");

											return;
										}
									}
								}
								if (availStatus == null) {
									availStatus = true;
								}
								sendQueryResults();
								setStatus(CapabilityStatus.COMPLETE,
										"QueryCalendar complete");
								return;
							}
						}
					} else {
						personFullName = "Unknown";
						roomNumber = "";
						availStatus = false;
						sendQueryResults();
					}

				} catch (final MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (final IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (final ServiceException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void terminate() {
		if (getStatus() != CapabilityStatus.COMPLETE) {
			setStatus(CapabilityStatus.CANCELED, "Terminated");
		}

	}

	@Override
	public String getCapabilityName() {
		// TODO Auto-generated method stub
		return NAME;
	}

	@Override
	public String getCapabilityDescription() {
		// TODO Auto-generated method stub
		return DESCRIPTION;
	}

	private void sendQueryResults() {

		System.out.println("[QueryGoogleCalendarCapability] sending results");
		sendOutput(OUTPUT_ROOM_NUMBER, roomNumber);
		sendOutput("status", availStatus.toString());
		sendOutput("person", personFullName);
	}

	@Override
	public void setInput(final ConcurrentHashMap<Object, Object> input) {
		personName = null;
		personFullName = null;
		startTime = null;
		endTime = null;
		roomNumber = "";
		availStatus = false;

		if (input.containsKey(PERSON_NAME)) {
			personName = (String) input.get(PERSON_NAME);
		}
		if (input.containsKey(START_TIME)) {
			startTime = (String) input.get(START_TIME);
		}
		if (input.containsKey(END_TIME)) {
			endTime = (String) input.get(END_TIME);
		}
		newInputFromUser = true;
		System.out.println("[QueryGoogleCalendarCapability] got input");
	}

	@Override
	public Object getInputParameterValue(final Object param) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ConcurrentHashMap<String, Class> getInputRequirements() {
		final ConcurrentHashMap<String, Class> inputReqs = new ConcurrentHashMap<String, Class>();

		inputReqs.put(PERSON_NAME, String.class);
		inputReqs.put(START_TIME, String.class);
		inputReqs.put(END_TIME, String.class);
		return inputReqs;
	}

	// @Override
	public HashMap<Object, Object> pause() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ConcurrentHashMap<String, Class> getOutputRequirements() {
		final ConcurrentHashMap<String, Class> outputReqs = new ConcurrentHashMap<String, Class>();

		outputReqs.put(PERSON_FULL_NAME, String.class);
		outputReqs.put(AVAILABILITY_STATUS, String.class);
		outputReqs.put(ROOM_NUMBER, String.class);
		return outputReqs;
	}

	private ArrayList<String> readPersonnelData() {
		Scanner scanner = null;
		final ArrayList<String> persData = new ArrayList<String>();
		try {

			final String ppl = (String) getConfigurationValue(PERSONNEL_STORE);
			// String ppl = "personneldata.txt";
			scanner = new Scanner(new File(ppl));
			// scanner = new Scanner(new File("personneldata.txt"));
			int i = 0;
			while (scanner.hasNextLine()) {
				// The first line includes headings, which are not required
				if (i == 0) {
					scanner.nextLine();
				} else {
					persData.add(scanner.nextLine());
				}
				i++;
			}
		} catch (final FileNotFoundException e) {
			System.out
					.println("[QueryGoogleCalendarCapability] Cannot find the	personnel data file");
		} finally {
			if (scanner != null) {
				scanner.close();
			}
		}
		return persData;
	}
}
