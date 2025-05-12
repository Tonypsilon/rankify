import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of, throwError } from 'rxjs';
import { catchError, delay } from 'rxjs/operators';
import { Ballot } from '../models/poll.model';

@Injectable({
  providedIn: 'root'
})
export class BallotService {
  private apiUrl = '/api/ballots'; // Backend API URL for future implementation
  
  // Mock data for testing without backend
  private mockBallots: Ballot[] = [
    {
      id: 'b1',
      pollId: '1',
      rankings: [
        { optionId: '1-2', rank: 1 }, // Sushi Bar rank 1
        { optionId: '1-1', rank: 2 }, // Italian Restaurant rank 2
        { optionId: '1-3', rank: 3 }, // Burger Joint rank 3
        { optionId: '1-4', rank: 4 }  // Salad Place rank 4
      ]
    },
    {
      id: 'b2',
      pollId: '1',
      rankings: [
        { optionId: '1-1', rank: 1 }, // Italian Restaurant rank 1
        { optionId: '1-3', rank: 1 }, // Burger Joint also rank 1 (tie)
        { optionId: '1-2', rank: 2 }, // Sushi Bar rank 2
        { optionId: '1-4', rank: 3 }  // Salad Place rank 3
      ]
    }
  ];

  constructor(private http: HttpClient) {}

  /**
   * Casts a ballot for a poll
   * @param ballot The ballot with rankings to cast
   * @returns Observable with the submitted ballot
   */
  castBallot(ballot: Ballot): Observable<Ballot> {
    // MOCK IMPLEMENTATION
    // In a real implementation, replace with: 
    // return this.http.post<Ballot>(this.apiUrl, ballot).pipe(...)
    
    const newBallot: Ballot = {
      ...ballot,
      id: `b${Date.now()}`
    };
    
    this.mockBallots.push(newBallot);
    return of(newBallot).pipe(delay(500)); // Simulate network delay
  }

  /**
   * Gets all ballots for a specific poll
   * @param pollId The ID of the poll
   * @returns Observable with the list of ballots
   */
  getBallotsByPollId(pollId: string): Observable<Ballot[]> {
    // MOCK IMPLEMENTATION
    // In a real implementation, replace with: 
    // return this.http.get<Ballot[]>(`${this.apiUrl}/poll/${pollId}`).pipe(...)
    
    const ballots = this.mockBallots.filter(b => b.pollId === pollId);
    return of([...ballots]).pipe(delay(500));
  }
}
