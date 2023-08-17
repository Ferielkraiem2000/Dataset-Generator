import { IntervalData } from "./interval.interface";

export interface HistogramData {
    intervals: IntervalData[];
    segmentCountPerInterval: number[];
  }
  
