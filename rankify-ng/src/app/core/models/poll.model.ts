export interface Poll {
  id: string;
  name: string;
  isOpen: boolean;
  options: PollOption[];
  createdAt: Date;
}

export interface PollOption {
  id: string;
  name: string;
}

export interface Ballot {
  id: string;
  pollId: string;
  rankings: Ranking[];
}

export interface Ranking {
  optionId: string;
  rank: number;
}
