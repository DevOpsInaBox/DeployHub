/*
 *
 *  DeployHub is an Agile Application Release Automation Solution
 *  Copyright (C) 2017 Catalyst Systems Corporation DBA OpenMake Software
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as
 *  published by the Free Software Foundation, either version 3 of the
 *  License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 *
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package dmadmin.model;

import java.util.ArrayList;

import dmadmin.DMSession;
import dmadmin.ObjectType;
import dmadmin.PropertyDataSet;
import dmadmin.SummaryChangeSet;
import dmadmin.SummaryField;
import dmadmin.json.IJSONSerializable;

public class Server
	extends DMObject
{
	private static final long serialVersionUID = 1327862378913381548L;
	
	private String m_hostname;
	private String m_protocol;
	private String m_basedir;
	
	private int 	m_xpos;
	private int 	m_ypos;
	private String 	m_typename;
	private ServerType m_serverType;
	private Credential m_cred;
	private int m_sshPort;
	private ArrayList<CompType> m_servercomptype;
	
	private String formatTime(int s)
	{
		Integer Hours = (int)Math.floor(s / 3600);
		Integer Mins  = (int)Math.floor((s - (Hours * 3600)) / 60);
		String sHours = Hours.toString();
		if (Hours<10) sHours = "0"+sHours;
		String sMins  = Mins.toString();
		if (Mins<10) sMins = "0"+sMins;
		return sHours+":"+sMins;
	}
	
	private void init() {
		m_typename="";
	}
	public Server() {
		init();
	}
	
	public Server(DMSession sess, int id, String name) {
		super(sess, id, name);
		init();
	}
	
	
	@Override
	public ObjectType getObjectType() {
		return ObjectType.SERVER;
	}

	@Override
	public String getDatabaseTable() {
		return "dm_server";
	}

	@Override
	public String getForeignKey() {
		return "serverid";
	}
	
	public String getHostName()  { return m_hostname; }
	public void setHostName(String hostname)  { m_hostname = hostname; }
	
	public String getProtocol()  { return m_protocol; }
	public void setProtocol(String protocol)  { m_protocol = protocol; }
	
	public String getBaseDir()  { return m_basedir; }
	public void setBaseDir(String basedir)  { m_basedir = basedir; }
	
	public ServerType getServerType()  { return m_serverType; }
	public void setServerType(ServerType serverType)  { m_serverType = serverType; }
	
 public ArrayList<CompType> getServerCompType()  { return m_servercomptype; }
 public void setServerCompType(ArrayList<CompType> servercomptype)  { m_servercomptype = servercomptype; }
 
	public Credential getCredential()  { return m_cred; }
	public void setCredential(Credential cred)  { m_cred = cred; }
	
	public void setTypeName(String typename) {
		m_typename = typename;
	}
	
	public void setXpos(int xpos) {
		m_xpos = xpos;
	}
	
	public void setYpos(int ypos) {
		m_ypos = ypos;
	}
	
	public void setSSHPort(int portnum) {
		m_sshPort = portnum;
	}
	
	public String getTypeName() {
		return m_typename;
	}
	
	public int getXpos() {
		return m_xpos;
	}
	
	public int getYpos() {
		return m_ypos;
	}
	
	public int getSSHPort() {
		return m_sshPort;
	}
	
	public ServerStatus getServerStatus()
	{
		return m_session.getServerStatus(m_id);
	}
	
	public IJSONSerializable getStatusData() {
		PropertyDataSet ds = new PropertyDataSet();
		ServerStatus ss = getServerStatus();
		if (ss != null) {
			// Data for this server
			ds.addProperty(SummaryField.NAME_RESOLUTION, "Name Resolution", ss.getNameResolution());
			ds.addProperty(SummaryField.PING_SUCCESS, "Ping", ss.getPingStatus());
			ds.addProperty(SummaryField.CONNECT_SUCCESS, "Connection", ss.getConnectionStatus());
			ds.addProperty(SummaryField.BASEDIR_SUCCESS, "Base Directory Check", ss.getBaseDirStatus());
			if (ss.getLastTime() > 0) {
				ds.addProperty(SummaryField.PING_TIME, "Ping Time (ms)", ss.getPingTime());
			} else {
				ds.addProperty(SummaryField.PING_TIME, "Ping Time (ms)", "");
			}
			ds.addProperty(SummaryField.IP_ADDRESS, "IPv4 Address", ss.getIPAddress());
			if (ss.getLastTime() > 0) {
				ds.addProperty(SummaryField.LAST_PING_TIME, "Last Checked", m_session.formatDateToUserLocale(ss.getLastTime()));
			} else {
				ds.addProperty(SummaryField.LAST_PING_TIME, "Last Checked", "Never");
			}
			ds.addProperty(SummaryField.LAST_ERROR, "Last Error", ss.getLastError());
		} else {
			// No data stored for this server yet.
			ds.addProperty(SummaryField.NAME_RESOLUTION, "Name Resolution", "");
			ds.addProperty(SummaryField.PING_SUCCESS, "Ping", "");
			ds.addProperty(SummaryField.CONNECT_SUCCESS, "Connection", "");
			ds.addProperty(SummaryField.BASEDIR_SUCCESS, "Base Directory Check", "");
			ds.addProperty(SummaryField.PING_TIME, "Ping Time (ms)", "");
			ds.addProperty(SummaryField.IP_ADDRESS, "IPv4 Address", "");
			ds.addProperty(SummaryField.LAST_PING_TIME, "Last Checked", "Never");
			ds.addProperty(SummaryField.LAST_ERROR, "Last Error", "");
		}
		return ds.getJSON();
	}

	@Override
	public IJSONSerializable getSummaryJSON() {
		PropertyDataSet ds = new PropertyDataSet();
		ds.addProperty(SummaryField.NAME, "Name", getName());
		ds.addProperty(SummaryField.OWNER, "Owner", (m_owner != null) ? m_owner.getLinkJSON()
				: ((m_ownerGroup != null) ? m_ownerGroup.getLinkJSON() : null));
		ds.addProperty(SummaryField.SUMMARY, "Summary", getSummary());
		addCreatorModifier(ds);
		ds.addProperty(SummaryField.SERVER_TYPE, "End Point Type",
				(m_serverType != null) ? m_serverType.getLinkJSON() : null);
		ds.addProperty(SummaryField.SERVER_HOSTNAME, "Hostname", m_hostname);
		ds.addProperty(SummaryField.SERVER_PROTOCOL, "Protocol", m_protocol);
		ds.addProperty(SummaryField.SERVER_SSHPORT,"SSH Port Number",m_sshPort);
		ds.addProperty(SummaryField.SERVER_BASEDIR, "Base Directory", m_basedir);
		ds.addProperty(SummaryField.SERVER_CRED, "Credential", (m_cred != null) ? m_cred.getLinkJSON() : null);
		boolean found = false;
		
		if (m_servercomptype != null)
		{ 
		 for (int i=0;i<m_servercomptype.size();i++)
		 {
		  found = true;
		  ds.addProperty(SummaryField.SERVER_COMPTYPE, "End Point Component Types", m_servercomptype.get(i).getId() + ";" + m_servercomptype.get(i).getName());
		 }
		} 
		
		if (!found)
   ds.addProperty(SummaryField.SERVER_COMPTYPE, "End Point Component Types", "");
		return ds.getJSON();
	}

	@Override
	public boolean updateSummary(SummaryChangeSet changes) {
		return m_session.updateServer(this, changes);
	}
	
	@Override
	public boolean hasReadWrite() {
		return false;
	}
}
