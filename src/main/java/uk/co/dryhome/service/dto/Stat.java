package uk.co.dryhome.service.dto;

import java.math.BigDecimal;

public class Stat {
        private StatIndividual current;
        private StatIndividual last;

        private BigDecimal diff;

        public Stat() {
        }

        public Stat(StatIndividual current, StatIndividual last, BigDecimal diff) {
            this.current = current;
            this.last = last;
            this.diff = diff;
        }

        public StatIndividual getCurrent() {
            return current;
        }

        public void setCurrent(StatIndividual current) {
            this.current = current;
        }

        public StatIndividual getLast() {
            return last;
        }

        public void setLast(StatIndividual last) {
            this.last = last;
        }

        public BigDecimal getDiff() {
            return diff;
        }

        public void setDiff(BigDecimal diff) {
            this.diff = diff;
        }

        @Override
        public String toString() {
            return "Stat{" +
                "current=" + current +
                ", last=" + last +
                ", diff=" + diff +
                '}';
        }


    }
