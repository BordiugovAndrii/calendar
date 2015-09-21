package com.bordisoft.common;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@JsonDeserialize(builder = Event.Builder.class)
public class Event implements Serializable {

    private String name;
    private String description;
    private List<Person> attenders;
    private GregorianCalendar startTime;
    private GregorianCalendar endTime;
    private int uuid;

    private Event() {}

    private Event(Builder obj) {
        this.name = obj.name;
        this.description = obj.description;
        this.attenders = obj.attenders;
        this.startTime = obj.startTime;
        this.endTime = obj.endTime;
        this.uuid = obj.uuid;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<Person> getAttenders() {
        return attenders;
    }

    public GregorianCalendar getStartTime() {
        return startTime;
    }

    public GregorianCalendar getEndTime() {
        return endTime;
    }

    public int getUuid() { return uuid; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Event)) return false;

        Event event = (Event) o;

        if (getName() != null ? !getName().equals(event.getName()) : event.getName() != null) return false;
        if (getDescription() != null ? !getDescription().equals(event.getDescription()) : event.getDescription() != null)
            return false;
        if (getAttenders() != null ? !getAttenders().equals(event.getAttenders()) : event.getAttenders() != null)
            return false;
        if (getStartTime() != null ? !getStartTime().equals(event.getStartTime()) : event.getStartTime() != null)
            return false;
        return !(getEndTime() != null ? !getEndTime().equals(event.getEndTime()) : event.getEndTime() != null);

    }

    @Override
    public int hashCode() {
        int result = getName().hashCode();
        result = 31 * result + getDescription().hashCode();
        result = 31 * result + getAttenders().hashCode();
        result = 31 * result + getStartTime().hashCode();
        result = 31 * result + getEndTime().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Event{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", attenders=" + attenders +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", uuid='" + uuid + '\'' +
                '}';
    }

    @JsonPOJOBuilder(buildMethodName = "build", withPrefix = "")
    public static class Builder {
        private String name;
        private String description;
        private List<Person> attenders;
        private GregorianCalendar startTime;
        private GregorianCalendar endTime;
        private int uuid;

        public Builder() {}

        public Builder(Event event) {
            this.name = event.getName();
            this.description = event.getDescription();
            this.attenders = event.getAttenders();
            this.startTime = event.getStartTime();
            this.endTime = event.getEndTime();
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder startTime(String startTime) {
            this.startTime = this.getTime(startTime);
            return this;
        }

        public Builder endTime(String endTime) {
            this.endTime = this.getTime(endTime);
            return this;
        }

        public Builder attenders(List<Person> attenders) {
            this.attenders = attenders;
            return this;
        }

        public Builder uuid(int uuid) {
            this.uuid = uuid;
            return this;
        }

        public Event build() {
            uuid = generateUuid();
            return new Event(this);
        }

        private GregorianCalendar getTime(String time) {
            GregorianCalendar converted = null;
            try {
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                Date date = format.parse(time);
                converted = new GregorianCalendar();
                converted.setTime(date);
            }
            catch(Exception e) {
                try {
                    Date date = new Date(Long.parseLong(time));
                    converted = new GregorianCalendar();
                    converted.setTime(date);
                }
                catch(Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }

            return converted;
        }

        @Override
        public int hashCode() {
            int result = name != null ? name.hashCode() : 0;
            result = 31 * result + (description != null ? description.hashCode() : 0);
            result = 31 * result + (attenders != null ? attenders.hashCode() : 0);
            result = 31 * result + (startTime != null ? startTime.hashCode() : 0);
            result = 31 * result + (endTime != null ? endTime.hashCode() : 0);
            return result;
        }

        private int generateUuid() {
            return Math.abs(hashCode());
        }

    }
}
