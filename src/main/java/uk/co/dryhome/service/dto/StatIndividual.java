package uk.co.dryhome.service.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class StatIndividual {
        private int count;
        private BigDecimal total;

        private LocalDate start;
        private LocalDate end;

        public StatIndividual() {
        }

        public StatIndividual(int count, BigDecimal total, LocalDate start, LocalDate end) {
            this.count = count;
            this.total = total;
            this.start = start;
            this.end = end;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public BigDecimal getTotal() {
            return total;
        }

        public void setTotal(BigDecimal total) {
            this.total = total;
        }

        public LocalDate getStart() {
            return start;
        }

        public void setStart(LocalDate start) {
            this.start = start;
        }

        public LocalDate getEnd() {
            return end;
        }

        public void setEnd(LocalDate end) {
            this.end = end;
        }

        @Override
        public String toString() {
            return "StatIndividual{" +
                "count=" + count +
                ", total=" + total +
                ", start=" + start +
                ", end=" + end +
                '}';
        }


    }
